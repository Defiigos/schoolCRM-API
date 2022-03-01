package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.LessonGroupDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.StudentDto;
import com.defiigosProject.SchoolCRMBackend.exception.EntityAlreadyExistException;
import com.defiigosProject.SchoolCRMBackend.exception.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.EntityUsedException;
import com.defiigosProject.SchoolCRMBackend.exception.FieldRequiredException;
import com.defiigosProject.SchoolCRMBackend.model.LessonGroup;
import com.defiigosProject.SchoolCRMBackend.model.LessonGroupStatus;
import com.defiigosProject.SchoolCRMBackend.model.Student;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.LessonGroupRepo;
import com.defiigosProject.SchoolCRMBackend.repo.LessonGroupStatusRepo;
import com.defiigosProject.SchoolCRMBackend.repo.StudentRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.defiigosProject.SchoolCRMBackend.repo.Specification.LessonGroupSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class LessonGroupService {

    private final LessonGroupRepo lessonGroupRepo;
    private final LessonGroupStatusRepo lessonGroupStatusRepo;
    private final StudentRepo studentRepo;

    public LessonGroupService(LessonGroupRepo lessonGroupRepo,
                              LessonGroupStatusRepo lessonGroupStatusRepo, StudentRepo studentRepo) {
        this.lessonGroupRepo = lessonGroupRepo;
        this.lessonGroupStatusRepo = lessonGroupStatusRepo;
        this.studentRepo = studentRepo;
    }

    public ResponseEntity<MessageResponse> createLessonGroup(LessonGroupDto lessonGroupDto)
            throws FieldRequiredException, EntityAlreadyExistException, EntityNotFoundException {

        if (lessonGroupDto.getName() == null || lessonGroupDto.getName().isEmpty()){
            throw new FieldRequiredException("name");
        }

        if (lessonGroupRepo.existsByName(lessonGroupDto.getName())){
            throw new EntityAlreadyExistException("lesson group name");
        }

        LessonGroupStatus newStatus = lessonGroupStatusRepo.findByStatus(LessonGroupStatusType.LESSON_GROUP_ACTIVE)
                .orElseThrow(() -> new RuntimeException("Error, Lesson group status "
                        + LessonGroupStatusType.LESSON_GROUP_ACTIVE + " is not found"));

        LessonGroup newLessonGroup = new LessonGroup(lessonGroupDto.getName());
        newStatus.addLessonGroup(newLessonGroup);

        if (lessonGroupDto.getStudents() != null) {
            Set<StudentDto> studentDtos = lessonGroupDto.getStudents();

            for (StudentDto studentDto: studentDtos) {
                if (studentDto.getId() == null || studentDto.getId().toString().isEmpty())
                    throw new FieldRequiredException("student id");

                Student student = studentRepo.findById(studentDto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("student"));

                newLessonGroup.addStudent(student);
            }
        }

        lessonGroupRepo.save(newLessonGroup);
        return ResponseEntity.ok(new MessageResponse("Lesson group successfully created"));
    }

    public ResponseEntity<List<LessonGroupDto>> getLessonGroup(Long id, String name,
                                                               LessonGroupStatusType status, Long studentId) {

        List<LessonGroup> lessonGroupList = lessonGroupRepo.findAll(
                where(withId(id))
                        .and(withName(name))
                        .and(withStatus(status))
                        .and(withStudentId(studentId))
        );

        List<LessonGroupDto> lessonGroupDtoList = new ArrayList<>();
        for (LessonGroup lessonGroup: lessonGroupList) {
            Set<StudentDto> studentDtos = new HashSet<>();
            for (Student student: lessonGroup.getStudents()) {
                studentDtos.add(StudentDto.build(student));
            }
            lessonGroupDtoList.add(LessonGroupDto.build(lessonGroup));
        }
        return ResponseEntity.ok(lessonGroupDtoList);
    }

    public ResponseEntity<MessageResponse> updateLessonGroup(Long id, LessonGroupDto lessonGroupDto)
            throws EntityNotFoundException, FieldRequiredException, EntityAlreadyExistException {

        LessonGroup updatedLessonGroup = lessonGroupRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("lesson group with this id:" + id));

        if (lessonGroupDto.getName() != null) {
            if (lessonGroupDto.getName() == null || lessonGroupDto.getName().isEmpty()) {
                throw new FieldRequiredException("name");
            }
            if (!lessonGroupDto.getName().equals(updatedLessonGroup.getName())) {
                if (lessonGroupRepo.existsByName(lessonGroupDto.getName())) {
                    throw new EntityAlreadyExistException("lesson group name");
                }
                updatedLessonGroup.setName(lessonGroupDto.getName());
            }
        }

        if (lessonGroupDto.getStatus() != null
                || !lessonGroupDto.getStatus().equals(updatedLessonGroup.getStatus().getStatus())) {
            LessonGroupStatus oldStatus = lessonGroupStatusRepo
                    .findByStatus(updatedLessonGroup.getStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old lesson group status is not found"));
            oldStatus.removeLessonGroup(updatedLessonGroup);

            LessonGroupStatus newStatus = lessonGroupStatusRepo
                    .findByStatus(lessonGroupDto.getStatus())
                    .orElseThrow(() -> new EntityNotFoundException("lesson group status"));
            newStatus.addLessonGroup(updatedLessonGroup);
        }

        if (lessonGroupDto.getStudents() != null) {
            for (Student student: updatedLessonGroup.getStudents()){
                updatedLessonGroup.removeStudent(student);
            }

            Set<StudentDto> studentDtos = lessonGroupDto.getStudents();
            for (StudentDto studentDto: studentDtos) {
                if (studentDto.getId() == null || studentDto.getId().toString().isEmpty())
                    throw new FieldRequiredException("student id");

                Student student = studentRepo.findById(studentDto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("student"));

                updatedLessonGroup.addStudent(student);
            }
        }

        lessonGroupRepo.save(updatedLessonGroup);
        return ResponseEntity.ok(new MessageResponse("Lesson group successfully updated"));
    }

    public ResponseEntity<MessageResponse> addStudent(Long id, Long studentDto) throws EntityNotFoundException {

        LessonGroup updatedLessonGroup = lessonGroupRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("lesson group with this id:" + id));

        Student addStudent = studentRepo.findById(studentDto).
                orElseThrow(() -> new EntityNotFoundException("student with this id: " + studentDto));

        updatedLessonGroup.addStudent(addStudent);
        lessonGroupRepo.save(updatedLessonGroup);
        return ResponseEntity.ok(new MessageResponse("Student successfully add"));
    }

    public ResponseEntity<MessageResponse> removeStudent(Long id, Long studentId) throws EntityNotFoundException {

        LessonGroup updatedLessonGroup = lessonGroupRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("lesson group with this id:" + id));

        Student removeStudent = studentRepo.findById(studentId).
                orElseThrow(() -> new EntityNotFoundException("student with this id: " + studentId));

        updatedLessonGroup.removeStudent(removeStudent);
        lessonGroupRepo.save(updatedLessonGroup);
        return ResponseEntity.ok(new MessageResponse("Student successfully remove"));
    }

    public ResponseEntity<MessageResponse> deleteLessonGroup(Long id)
            throws EntityNotFoundException, EntityUsedException {

        LessonGroup deletedLessonGroup = lessonGroupRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("lesson group with this id:" + id));

        if (!deletedLessonGroup.getLessons().isEmpty())
            throw new EntityUsedException("lesson group", "lessons");

        if (!deletedLessonGroup.getStudents().isEmpty())
            throw new EntityUsedException("lesson group", "students");

        lessonGroupRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Lesson group successfully deleted"));
    }
}
