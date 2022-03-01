package com.defiigosProject.SchoolCRMBackend.controller;


import com.defiigosProject.SchoolCRMBackend.dto.LessonGroupDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.StudentDto;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.model.Student;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import com.defiigosProject.SchoolCRMBackend.service.LessonGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons/groups")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonGroupController {

    private final LessonGroupService lessonGroupService;

    public LessonGroupController(LessonGroupService lessonGroupService) {
        this.lessonGroupService = lessonGroupService;
    }

//    TODO авторизация ? только так ? -> @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<LessonGroupDto>> getLessonGroup(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status
    ) throws BadEnumException {
        try {
            if (status != null)
                return lessonGroupService.getLessonGroup(id, name, LessonGroupStatusType.valueOf(status));
            else
                return lessonGroupService.getLessonGroup(id, name, null);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(LessonGroupStatusType.class, status);
        }
    }

//    TODO авторизация ? только так ? -> @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createLessonGroup(@RequestBody LessonGroupDto lessonGroupDto)
            throws FieldRequiredException, EntityAlreadyExistException, EntityNotFoundException {
        return lessonGroupService.createLessonGroup(lessonGroupDto);
    }

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateLessonGroup(
            @PathVariable(value = "id") Long id,
            @RequestBody LessonGroupDto lessonGroupDto
    ) throws EntityNotFoundException, EntityAlreadyExistException, FieldRequiredException {
        return lessonGroupService.updateLessonGroup(id, lessonGroupDto);
    }

    @PostMapping("/{id}/student/{studentId}")
    public ResponseEntity<MessageResponse> addStudent(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "studentId") Long studentId
            ) throws EntityNotFoundException, FieldRequiredException {
        return lessonGroupService.addStudent(id, studentId);
    }

    @DeleteMapping("/{id}/student/{studentId}")
    public ResponseEntity<MessageResponse> removeStudent(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "studentId") Long studentId
    ) throws EntityNotFoundException, FieldRequiredException {
        return lessonGroupService.removeStudent(id, studentId);
    }

//    TODO авторизация hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteLessonGroup(
            @PathVariable(value = "id") Long id
    ) throws EntityNotFoundException, EntityUsedException {
        return lessonGroupService.deleteLessonGroup(id);
    }
}
