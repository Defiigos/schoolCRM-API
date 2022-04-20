package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.lesson.LessonGroupDto;
import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.StudentDto;
import com.defiigosProject.SchoolCRMBackend.dto.lesson.UpdateLessonGroupDto;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.model.LessonGroup;
import com.defiigosProject.SchoolCRMBackend.model.LessonGroupStatus;
import com.defiigosProject.SchoolCRMBackend.model.Student;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.LessonGroupRepo;
import com.defiigosProject.SchoolCRMBackend.repo.LessonGroupStatusRepo;
import com.defiigosProject.SchoolCRMBackend.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType.LESSON_GROUP_ACTIVE;
import static com.defiigosProject.SchoolCRMBackend.repo.Specification.LessonGroupSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class LessonGroupService {

    private final String uri;
    private final LessonGroupRepo lessonGroupRepo;
    private final LessonGroupStatusRepo lessonGroupStatusRepo;
    private final StudentRepo studentRepo;

    public LessonGroupService(@Value("${hostname}") String uri, LessonGroupRepo lessonGroupRepo,
                              LessonGroupStatusRepo lessonGroupStatusRepo, StudentRepo studentRepo) {
        this.uri = uri;
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

        LessonGroupStatus newStatus = lessonGroupStatusRepo.findByStatus(LESSON_GROUP_ACTIVE)
                .orElseThrow(() -> new RuntimeException("Error, Lesson group status "
                        + LESSON_GROUP_ACTIVE + " is not found"));

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
        return ResponseEntity.created(URI.create(uri + "/api/lessons/groups"))
                .body(new MessageResponse("Lesson group successfully created"));
    }

    public ResponseEntity<List<LessonGroupDto>> getLessonGroup(Long id, String name, String status,
                                                               Long lessonId, Long studentId, String studentName)
            throws BadEnumException {

        LessonGroupStatusType paresStatus = null;
        try {
            if (status != null)
                paresStatus = LessonGroupStatusType.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(LessonGroupStatusType.class, status);
        }

        List<LessonGroup> lessonGroupList = lessonGroupRepo.findAll(
                where(withId(id))
                        .and(withName(name))
                        .and(withStatus(paresStatus))
                        .and(withLessonId(lessonId))
                        .and(withStudentId(studentId))
                        .and(withStudentName(studentName)),
                Sort.by(Sort.Direction.ASC, "id")
        );

        List<LessonGroupDto> lessonGroupDtoList = new ArrayList<>();
        for (LessonGroup lessonGroup: lessonGroupList) {
            lessonGroupDtoList.add(LessonGroupDto.build(lessonGroup));
        }
        return ResponseEntity.ok(lessonGroupDtoList);
    }

    public ResponseEntity<MessageResponse> updateLessonGroup(Long id, UpdateLessonGroupDto lessonGroupDto)
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

        lessonGroupRepo.save(updatedLessonGroup);
        return ResponseEntity.ok(new MessageResponse("Lesson group successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteLessonGroup(Long id)
            throws EntityNotFoundException, EntityUsedException {

        LessonGroup deletedLessonGroup = lessonGroupRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("lesson group with this id:" + id));

        if (!deletedLessonGroup.getLessons().isEmpty())
            throw new EntityUsedException("lesson group", "lessons");

        Set<Student> students = new HashSet<>(deletedLessonGroup.getStudents());
        for (Student student: students) {
            deletedLessonGroup.removeStudent(student);
        }

        lessonGroupRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Lesson group successfully deleted"));
    }
}
