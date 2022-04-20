package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.StudentDto;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.model.Student;
import com.defiigosProject.SchoolCRMBackend.model.StudentStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.StudentStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.StudentRepo;
import com.defiigosProject.SchoolCRMBackend.repo.StudentStatusRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.defiigosProject.SchoolCRMBackend.model.enumerated.StudentStatusType.STUDENT_ACTIVE;
import static com.defiigosProject.SchoolCRMBackend.repo.Specification.StudentSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class StudentService {

    private final String uri;
    private final StudentRepo studentRepo;
    private final StudentStatusRepo studentStatusRepo;

    public StudentService(@Value("${hostname}") String uri, StudentRepo studentRepo, StudentStatusRepo studentStatusRepo) {
        this.uri = uri;
        this.studentRepo = studentRepo;
        this.studentStatusRepo = studentStatusRepo;
    }

    public ResponseEntity<MessageResponse> createStudent(StudentDto studentDto)
            throws FieldRequiredException {

        if (studentDto.getParentName() == null || studentDto.getParentName().isEmpty()){
            throw new FieldRequiredException("parentName");
        }

        if (studentDto.getParentPhone() == null || studentDto.getParentPhone().isEmpty()){
            throw new FieldRequiredException("parentPhone");
        }

        StudentStatus activeStatus = studentStatusRepo.findByStatus(STUDENT_ACTIVE)
                .orElseThrow(() -> new RuntimeException("Error, Student status "
                        + STUDENT_ACTIVE + " is not found"));

        Student newStudent = new Student(
                studentDto.getName(),
                studentDto.getPhone(),
                studentDto.getParentName(),
                studentDto.getParentPhone(),
                studentDto.getDescription());
        activeStatus.addStudent(newStudent);

        studentRepo.save(newStudent);
        return ResponseEntity.created(URI.create(uri + "/api/students"))
                .body(new MessageResponse("Student successfully created"));
    }

    public ResponseEntity<List<StudentDto>> getStudent(Long id, String name, String phone,
                                                       String parentName, String parentPhone,
                                                       String description, String status)
            throws BadEnumException {

        StudentStatusType parseStatus = null;
        try {
            if (status != null)
                 parseStatus = StudentStatusType.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(StudentStatusType.class, status);
        }

        List<Student> studentList = studentRepo.findAll(
                where(withId(id))
                        .and(withName(name))
                        .and(withPhone(phone))
                        .and(withParentName(parentName))
                        .and(withParentPhone(parentPhone))
                        .and(withDescription(description))
                        .and(withStatus(parseStatus)),
                Sort.by(Sort.Direction.ASC, "id")
        );

        List<StudentDto> studentDtoList = new ArrayList<>();
        for (Student student: studentList) {
                studentDtoList.add(StudentDto.build(student));
        }

        return ResponseEntity.ok(studentDtoList);
    }

    public ResponseEntity<MessageResponse> updateStudent(Long id, StudentDto studentDto)
            throws EntityNotFoundException, FieldNotNullException {

        Student updatedStudent = studentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("location with this id:" + id));

        if (studentDto.getParentName() != null){
            if (studentDto.getParentName().isEmpty())
                throw new FieldNotNullException("parentName");
            updatedStudent.setParentName(studentDto.getParentName());
        }

        if (studentDto.getParentPhone() != null){
            if (studentDto.getParentPhone().isEmpty())
                throw new FieldNotNullException("parentPhone");
            updatedStudent.setParentPhone(studentDto.getParentPhone());
        }

        if (studentDto.getName() != null){
            updatedStudent.setName(studentDto.getName());
        }

        if (studentDto.getPhone() != null){
            updatedStudent.setPhone(studentDto.getPhone());
        }

        if (studentDto.getDescription() != null){
            updatedStudent.setDescription(studentDto.getDescription());
        }

        if (studentDto.getStatus() != null) {
            StudentStatus oldStatus = studentStatusRepo
                    .findByStatus(updatedStudent.getStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old student status is not found"));
            oldStatus.removeStudent(updatedStudent);

            StudentStatus newStatus = studentStatusRepo
                    .findByStatus(studentDto.getStatus())
                    .orElseThrow(() -> new EntityNotFoundException("student status"));
            newStatus.addStudent(updatedStudent);
        }

        studentRepo.save(updatedStudent);
        return ResponseEntity.ok(new MessageResponse("Student successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteStudent(Long id)
            throws EntityNotFoundException, EntityUsedException {

        Student deletedStudent = studentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("student with this id:" + id));

        if (!deletedStudent.getLessonGroups().isEmpty())
            throw new EntityUsedException("student", "lesson group");

        if (!deletedStudent.getPayments().isEmpty())
            throw new EntityUsedException("student", "payment");

        studentRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Student successfully deleted"));
    }
}
