package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.LessonCreateRequest;
import com.defiigosProject.SchoolCRMBackend.dto.LessonDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.exception.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.FieldRequiredException;
import com.defiigosProject.SchoolCRMBackend.model.*;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.defiigosProject.SchoolCRMBackend.repo.Specification.LessonSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class LessonService {

    private final LessonRepo lessonRepo;
    private final LessonStatusRepo lessonStatusRepo;
    private final UserRepo userRepo;
    private final LessonDurationRepo lessonDurationRepo;
    private final LocationRepo locationRepo;
    private final LessonGroupRepo lessonGroupRepo;
    private final PaymentAmountRepo paymentAmountRepo;
    private final PaymentService paymentService;
    private final PaymentRepo paymentRepo;

    public LessonService(LessonRepo lessonRepo, LessonStatusRepo
            lessonStatusRepo, UserRepo userRepo, LessonDurationRepo lessonDurationRepo,
                         LocationRepo locationRepo, LessonGroupRepo lessonGroupRepo,
                         PaymentAmountRepo paymentAmountRepo, PaymentService paymentService, PaymentRepo paymentRepo) {
        this.lessonRepo = lessonRepo;
        this.lessonStatusRepo = lessonStatusRepo;
        this.userRepo = userRepo;
        this.lessonDurationRepo = lessonDurationRepo;
        this.locationRepo = locationRepo;
        this.lessonGroupRepo = lessonGroupRepo;
        this.paymentAmountRepo = paymentAmountRepo;
        this.paymentService = paymentService;
        this.paymentRepo = paymentRepo;
    }

    public ResponseEntity<MessageResponse> createLesson(LessonCreateRequest request)
            throws EntityNotFoundException, FieldRequiredException, IllegalAccessException {

        for (Field field: request.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if (field.get(request) == null)
                throw new FieldRequiredException(field.getName());
            try {
                Field idField = field.get(request).getClass().getDeclaredField("id");
                idField.setAccessible(true);
                if (idField.get(field.get(request)) == null)
                    throw new FieldRequiredException(field.getName() + " id");
            } catch (NoSuchFieldException e) { continue; }
        }

        Lesson newLesson = new Lesson(
                request.getDate(),
                request.getTime()
        );

        LessonStatus newStatus = lessonStatusRepo.findByStatus(LessonStatusType.LESSON_COMING)
                .orElseThrow(() -> new RuntimeException("Error, Lesson status "
                        + LessonStatusType.LESSON_COMING + " is not found"));

        LessonDuration lessonDuration = lessonDurationRepo.findById(request.getLessonDuration().getId())
                .orElseThrow(() -> new EntityNotFoundException("lesson duration"));

        Location location = locationRepo.findById(request.getLocation().getId())
                .orElseThrow(() -> new EntityNotFoundException("location"));

        User teacher = userRepo.findById(request.getTeacher().getId())
                .orElseThrow(() -> new EntityNotFoundException("user"));

        LessonGroup lessonGroup  = lessonGroupRepo.findById(request.getLessonGroup().getId())
                .orElseThrow(() -> new EntityNotFoundException("lesson group"));

        PaymentAmount paymentAmount = paymentAmountRepo.findById(request.getPaymentAmount().getId())
                .orElseThrow(() -> new EntityNotFoundException("payment amount"));

        newStatus.addLesson(newLesson);
        lessonDuration.addLesson(newLesson);
        location.addLesson(newLesson);
        teacher.addLesson(newLesson);
        lessonGroup.addLesson(newLesson);

        StringBuilder paymentAnswer = new StringBuilder(" \n ");
        for (Student student: lessonGroup.getStudents())
            paymentAnswer.append(paymentService.createPayment(newLesson, student, paymentAmount)).append("\n ");

        return ResponseEntity.ok(new MessageResponse("Lesson successfully created!" + paymentAnswer));
    }

    public ResponseEntity<List<LessonDto>> getLesson(Long id, String date, String time, LessonStatusType status,
                                                     Long teacherId, Long durationId, Long locationId) {

        List<Lesson> lessonList = lessonRepo.findAll(
                where(withId(id))
                        .and(withDate(date))
                        .and(withTime(time))
                        .and(withStatus(status))
                        .and(withTeacherId(teacherId))
                        .and(withDurationId(durationId))
                        .and(withLocationId(locationId))
        );

        List<LessonDto> lessonDtoList = new ArrayList<>();
        for (Lesson lesson: lessonList){
            lessonDtoList.add(LessonDto.build(lesson));
        }
        return ResponseEntity.ok(lessonDtoList);
    }

    public ResponseEntity<MessageResponse> updateLesson(Long id, LessonDto lessonDto)
            throws EntityNotFoundException, FieldRequiredException, BadRequestException {

        Lesson updatedLesson = lessonRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("lesson with this id:" + id));

        if (lessonDto.getDate() != null){
            updatedLesson.setDate(lessonDto.getDate());
        }

        if (lessonDto.getTime() != null){
            updatedLesson.setTime(lessonDto.getTime());
        }

        if (lessonDto.getTeacherDto() != null) {
            if (lessonDto.getTeacherDto().getId() == null || lessonDto.getTeacherDto().getId().toString().isEmpty())
                throw new FieldRequiredException("teacher id");

            User oldUser = userRepo
                    .findById(lessonDto.getTeacherDto().getId())
                    .orElseThrow(() -> new RuntimeException("Error, Old teacher is not found"));

            User newUser = userRepo
                    .findById(lessonDto.getTeacherDto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("teacher"));

            oldUser.removeLesson(updatedLesson);
            newUser.addLesson(updatedLesson);
        }

        if (lessonDto.getStatus() != null) {
            LessonStatus oldStatus = lessonStatusRepo
                    .findByStatus(updatedLesson.getStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old lesson status is not found"));

            LessonStatus newStatus = lessonStatusRepo
                    .findByStatus(lessonDto.getStatus())
                    .orElseThrow(() -> new EntityNotFoundException("lesson status"));

            oldStatus.removeLesson(updatedLesson);
            newStatus.addLesson(updatedLesson);
        }

        if (lessonDto.getDurationDto() != null) {
            if (lessonDto.getDurationDto().getId() == null || lessonDto.getDurationDto().getId().toString().isEmpty())
                throw new FieldRequiredException("lesson duration id");

            LessonDuration oldDuration = lessonDurationRepo
                    .findById(lessonDto.getDurationDto().getId())
                    .orElseThrow(() -> new RuntimeException("Error, Old lesson duration is not found"));

            LessonDuration newDuration = lessonDurationRepo
                    .findById(lessonDto.getDurationDto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("lesson duration"));

            oldDuration.removeLesson(updatedLesson);
            newDuration.addLesson(updatedLesson);
        }

        if (lessonDto.getLocationDto() != null) {
            if (lessonDto.getLocationDto().getId() == null || lessonDto.getLocationDto().getId().toString().isEmpty())
                throw new FieldRequiredException("location id");

            Location oldLocation = locationRepo
                    .findById(lessonDto.getLocationDto().getId())
                    .orElseThrow(() -> new RuntimeException("Error, Old lesson location is not found"));

            Location newLocation = locationRepo
                    .findById(lessonDto.getLocationDto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("location"));

            oldLocation.removeLesson(updatedLesson);
            newLocation.addLesson(updatedLesson);
        }

        if (lessonDto.getLessonGroupDto() != null) {
            if (lessonDto.getLessonGroupDto().getId() == null
                    || lessonDto.getLessonGroupDto().getId().toString().isEmpty())
                throw new FieldRequiredException("lesson group id");

            LessonGroup oldLessonGroup = lessonGroupRepo
                    .findById(lessonDto.getLessonGroupDto().getId())
                    .orElseThrow(() -> new RuntimeException("Error, Old lesson group is not found"));

            LessonGroup newLessonGroup = lessonGroupRepo
                    .findById(lessonDto.getLessonGroupDto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("lesson group"));

//            НАДЕЮСЬ ЭТО БУДЕТ РАБОТАТЬ
            PaymentAmount paymentAmount = null;
            for (Student student: oldLessonGroup.getStudents()){
                Payment studentPayment = paymentRepo.findByStudentIdAndLessonId(student.getId(), updatedLesson.getId())
                        .orElseThrow(() -> new EntityNotFoundException("student payment"));

                if (studentPayment.getStatus().getStatus().equals(PaymentStatusType.PAYMENT_PAID))
                    throw new BadRequestException("Could not remove payment, student have paid lesson");

                paymentAmount = studentPayment.getAmount();

                student.removePayment(studentPayment);
            }

            for (Student student : newLessonGroup.getStudents()) {
                paymentService.createPayment(updatedLesson, student, paymentAmount);
            }

            oldLessonGroup.removeLesson(updatedLesson);
            newLessonGroup.addLesson(updatedLesson);
        }

        lessonRepo.save(updatedLesson);
        return ResponseEntity.ok(new MessageResponse("Lesson successfully updated"));
    }

//    public ResponseEntity<MessageResponse> deleteLesson(Long id) throws EntityNotFoundException, EntityUsedException {
//
//         Lesson deletedLesson = lessonRepo.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("lesson with this id:" + id));
//
//        if (!deletedLesson.getPayments().isEmpty())
//            throw new EntityUsedException("lesson", "payment");
//
//        lessonRepo.deleteById(id);
//        return ResponseEntity.ok(new MessageResponse("Lesson successfully deleted"));
//    }
}
