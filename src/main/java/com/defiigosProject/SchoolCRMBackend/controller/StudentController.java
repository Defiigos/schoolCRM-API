package com.defiigosProject.SchoolCRMBackend.controller;


import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.StudentDto;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.StudentStatusType;
import com.defiigosProject.SchoolCRMBackend.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<StudentDto>> getStudent(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "parentName", required = false) String parentName,
            @RequestParam(value = "parentPhone", required = false) String parentPhone,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status
    ) throws BadEnumException {
        try {
            if (status != null)
                return studentService.getStudent(id, name, phone, parentName, parentPhone, description, StudentStatusType.valueOf(status));
            else
                return studentService.getStudent(id, name, phone, parentName, parentPhone, description, null);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(StudentStatusType.class, status);
        }
    }

//    TODO авторизация ? только так ? -> @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createStudent(@RequestBody StudentDto studentDto)
            throws FieldRequiredException {
        return studentService.createStudent(studentDto);
    }

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateStudent(
            @PathVariable(value = "id") Long id,
            @RequestBody StudentDto studentDto
    ) throws EntityNotFoundException, FieldNotNullException {
        return studentService.updateStudent(id, studentDto);
    }

//    TODO авторизация hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteStudent(
            @PathVariable(value = "id") Long id
    ) throws EntityNotFoundException, EntityUsedException {
        return studentService.deleteStudent(id);
    }
}
