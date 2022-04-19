package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.lesson.LessonCreateDto;
import com.defiigosProject.SchoolCRMBackend.dto.lesson.LessonDto;
import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadEnumException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.FieldRequiredException;
import com.defiigosProject.SchoolCRMBackend.model.*;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType.LESSON_CANCELED;
import static com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType.LESSON_COMING;
import static com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType.*;
import static com.defiigosProject.SchoolCRMBackend.repo.Specification.LessonSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class LessonService {

    private final String uri;
    private final LessonRepo lessonRepo;
    private final LessonStatusRepo lessonStatusRepo;
    private final UserRepo userRepo;
    private final LessonDurationRepo lessonDurationRepo;
    private final LocationRepo locationRepo;
    private final LessonGroupRepo lessonGroupRepo;
    private final PaymentAmountRepo paymentAmountRepo;
    private final PaymentService paymentService;
    private final PaymentRepo paymentRepo;
    private final PaymentStatusRepo paymentStatusRepo;

    public LessonService(@Value("${URI}") String uri, LessonRepo lessonRepo, LessonStatusRepo lessonStatusRepo, UserRepo userRepo,
                         LessonDurationRepo lessonDurationRepo, LocationRepo locationRepo,
                         LessonGroupRepo lessonGroupRepo, PaymentAmountRepo paymentAmountRepo,
                         PaymentService paymentService, PaymentRepo paymentRepo, PaymentStatusRepo paymentStatusRepo) {
        this.uri = uri;
        this.lessonRepo = lessonRepo;
        this.lessonStatusRepo = lessonStatusRepo;
        this.userRepo = userRepo;
        this.lessonDurationRepo = lessonDurationRepo;
        this.locationRepo = locationRepo;
        this.lessonGroupRepo = lessonGroupRepo;
        this.paymentAmountRepo = paymentAmountRepo;
        this.paymentService = paymentService;
        this.paymentRepo = paymentRepo;
        this.paymentStatusRepo = paymentStatusRepo;
    }

    public ResponseEntity<MessageResponse> createLesson(LessonCreateDto request)
            throws EntityNotFoundException, FieldRequiredException, IllegalAccessException, BadRequestException {

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

        LessonGroup lessonGroup  = lessonGroupRepo.findById(request.getLessonGroup().getId())
                .orElseThrow(() -> new EntityNotFoundException("lesson group"));

        if (lessonGroup.getStudents().isEmpty())
            throw new BadRequestException("lesson must have at least one student");

        LessonStatus newStatus = lessonStatusRepo.findByStatus(LESSON_COMING)
                .orElseThrow(() -> new RuntimeException("Error, Lesson status " + LESSON_COMING + " is not found"));

        LessonDuration lessonDuration = lessonDurationRepo.findById(request.getLessonDuration().getId())
                .orElseThrow(() -> new EntityNotFoundException("lesson duration"));

        Location location = locationRepo.findById(request.getLocation().getId())
                .orElseThrow(() -> new EntityNotFoundException("location"));

        User teacher = userRepo.findById(request.getTeacher().getId())
                .orElseThrow(() -> new EntityNotFoundException("user"));

        PaymentAmount paymentAmount = paymentAmountRepo.findById(request.getPaymentAmount().getId())
                .orElseThrow(() -> new EntityNotFoundException("payment amount"));

        Lesson newLesson = new Lesson(
                request.getDate(),
                request.getTime()
        );
        newStatus.addLesson(newLesson);
        lessonDuration.addLesson(newLesson);
        location.addLesson(newLesson);
        teacher.addLesson(newLesson);
        lessonGroup.addLesson(newLesson);

        StringBuilder paymentAnswer = new StringBuilder(" \n ");
        for (Student student: lessonGroup.getStudents())
            paymentAnswer.append(paymentService.createPayment(newLesson, student, paymentAmount)).append(" \n ");

        return ResponseEntity.created(URI.create(uri + "/api/lessons"))
                .body(new MessageResponse("Lesson successfully created!" + paymentAnswer));
    }

    public ResponseEntity<List<LessonDto>> getLesson(Long id, String date, String time, String status,
                                                     Long teacherId, Long durationId, Long locationId,
                                                     Long lessonGroupId,
                                                     String teacherName, String durationName, String locationName,
                                                     String lessonGroupName,
                                                     String dateFrom, String dateTo, String timeFrom, String timeTo)
            throws BadEnumException {

        LessonStatusType parseStatus = null;
        try {
            if (status != null)
                parseStatus = LessonStatusType.valueOf(status);

        } catch (IllegalArgumentException e) {
            throw new BadEnumException(LessonGroupStatusType.class, status);
        }

        List<Lesson> lessonList = lessonRepo.findAll(
                where(withId(id))
                        .and(withDate(date))
                        .and(withTime(time))
                        .and(withStatus(parseStatus))
                        .and(withTeacherId(teacherId))
                        .and(withDurationId(durationId))
                        .and(withLocationId(locationId))
                        .and(withLessonGroupId(lessonGroupId))
                        .and(withTeacherName(teacherName))
                        .and(withDurationName(durationName))
                        .and(withLocationName(locationName))
                        .and(withLessonGroupName(lessonGroupName))
                        .and(withDateFrom(dateFrom))
                        .and(withDateTo(dateTo))
                        .and(withTimeFrom(timeFrom))
                        .and(withTimeTo(timeTo)),
                Sort.by(Sort.Direction.ASC, "id")
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

        if (lessonDto.getDate() != null && (lessonDto.getDate() != updatedLesson.getDate())){
            updatedLesson.setDate(lessonDto.getDate());
        }

        if (lessonDto.getTime() != null && (lessonDto.getTime() != updatedLesson.getTime())){
            updatedLesson.setTime(lessonDto.getTime());
        }

        if (lessonDto.getTeacher() != null) {
            if (lessonDto.getTeacher().getId() == null || lessonDto.getTeacher().getId().toString().isEmpty())
                throw new FieldRequiredException("teacher id");

            User oldUser = userRepo
                    .findById(lessonDto.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Error, Old teacher is not found"));

            User newUser = userRepo
                    .findById(lessonDto.getTeacher().getId())
                    .orElseThrow(() -> new EntityNotFoundException("teacher"));

            oldUser.removeLesson(updatedLesson);
            newUser.addLesson(updatedLesson);
        }

        if (lessonDto.getStatus() != null && (lessonDto.getStatus() != updatedLesson.getStatus().getStatus())) {
            LessonStatus oldStatus = lessonStatusRepo
                    .findByStatus(updatedLesson.getStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old lesson status is not found"));

            LessonStatus newStatus = lessonStatusRepo
                    .findByStatus(lessonDto.getStatus())
                    .orElseThrow(() -> new EntityNotFoundException("lesson status"));

            PaymentStatus newPaymentStatus;
            if (newStatus.getStatus().equals(LESSON_CANCELED)) {
                newPaymentStatus = paymentStatusRepo.findByStatus(PAYMENT_CANCELED).
                        orElseThrow(() -> new RuntimeException("Error, payment status "
                                + PAYMENT_CANCELED + " is not found"));
            } else
                newPaymentStatus = paymentStatusRepo.findByStatus(PAYMENT_UNPAID).
                        orElseThrow(() -> new RuntimeException("Error, payment status "
                                + PAYMENT_UNPAID + " is not found"));

                for (Student student: updatedLesson.getLessonGroup().getStudents()){
                    Payment payment = paymentRepo.findByStudentIdAndLessonId(student.getId(), updatedLesson.getId())
                            .orElseThrow(() -> new EntityNotFoundException("payment"));

                    PaymentStatus oldPaymentStatus = paymentStatusRepo.findByStatus(payment.getStatus().getStatus()).
                            orElseThrow(() -> new RuntimeException("Error, old payment status is not found"));

                    if (oldPaymentStatus.getStatus().equals(PAYMENT_PAID))
                        throw new BadRequestException("Could not update payment status, " +
                                "student must not have paid lesson");

                    oldPaymentStatus.removePayment(payment);
                    newPaymentStatus.addPayment(payment);
                }

            oldStatus.removeLesson(updatedLesson);
            newStatus.addLesson(updatedLesson);
        }

        if (lessonDto.getDuration() != null) {
            if (lessonDto.getDuration().getId() == null || lessonDto.getDuration().getId().toString().isEmpty())
                throw new FieldRequiredException("lesson duration id");

            LessonDuration oldDuration = lessonDurationRepo
                    .findById(lessonDto.getDuration().getId())
                    .orElseThrow(() -> new RuntimeException("Error, Old lesson duration is not found"));

            LessonDuration newDuration = lessonDurationRepo
                    .findById(lessonDto.getDuration().getId())
                    .orElseThrow(() -> new EntityNotFoundException("lesson duration"));

            oldDuration.removeLesson(updatedLesson);
            newDuration.addLesson(updatedLesson);
        }

        if (lessonDto.getLocation() != null) {
            if (lessonDto.getLocation().getId() == null || lessonDto.getLocation().getId().toString().isEmpty())
                throw new FieldRequiredException("location id");

            Location oldLocation = locationRepo
                    .findById(lessonDto.getLocation().getId())
                    .orElseThrow(() -> new RuntimeException("Error, Old lesson location is not found"));

            Location newLocation = locationRepo
                    .findById(lessonDto.getLocation().getId())
                    .orElseThrow(() -> new EntityNotFoundException("location"));

            oldLocation.removeLesson(updatedLesson);
            newLocation.addLesson(updatedLesson);
        }

//        if (lessonDto.getLessonGroup() != null) {
//            if (lessonDto.getLessonGroup().getId() == null
//                    || lessonDto.getLessonGroup().getId().toString().isEmpty())
//                throw new FieldRequiredException("lesson group id");
//
//            LessonGroup oldLessonGroup = lessonGroupRepo
//                    .findById(lessonDto.getLessonGroup().getId())
//                    .orElseThrow(() -> new RuntimeException("Error, Old lesson group is not found"));
//
//            LessonGroup newLessonGroup = lessonGroupRepo
//                    .findById(lessonDto.getLessonGroup().getId())
//                    .orElseThrow(() -> new EntityNotFoundException("lesson group"));
//
//            PaymentAmount paymentAmount = null;
//            for (Student student: oldLessonGroup.getStudents()){
//                Payment payment = paymentRepo.findByStudentIdAndLessonId(student.getId(), updatedLesson.getId())
//                        .orElseThrow(() -> new EntityNotFoundException("payment"));
//
//                if (payment.getStatus().getStatus().equals(PAYMENT_PAID))
//                    throw new BadRequestException("Could not remove payment, student must not have paid lesson");
//
//                paymentAmount = payment.getAmount();
//
//                student.removePayment(payment);
//            }
//
//            for (Student student : newLessonGroup.getStudents()) {
//                paymentService.createPayment(updatedLesson, student, paymentAmount);
//            }
//
//            oldLessonGroup.removeLesson(updatedLesson);
//            newLessonGroup.addLesson(updatedLesson);
//        }

        lessonRepo.save(updatedLesson);
        return ResponseEntity.ok(new MessageResponse("Lesson successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteLesson(Long id)
            throws EntityNotFoundException, BadRequestException {

         Lesson deletedLesson = lessonRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("lesson with this id:" + id));

         for (Payment payment: deletedLesson.getPayments()){
             if (payment.getStatus().getStatus().equals(PAYMENT_PAID)){
                 throw new BadRequestException("Could not remove lesson, lesson must not have paid payment!");
             }
         }

        Set<Payment> payments = new HashSet<>(deletedLesson.getPayments());
        for (Payment payment: payments) {
            deletedLesson.removePayment(payment);
        }

        lessonRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Lesson successfully deleted"));
    }
}
