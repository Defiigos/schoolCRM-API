package com.defiigosProject.SchoolCRMBackend.controller;


import com.defiigosProject.SchoolCRMBackend.dto.lesson.LessonGroupDto;
import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.lesson.UpdateLessonGroupDto;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.service.LessonGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping()
    public ResponseEntity<List<LessonGroupDto>> getLessonGroup(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "lessonId", required = false) Long lessonId,
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "studentName", required = false) String studentName
            )
            throws BadEnumException {
        return lessonGroupService.getLessonGroup(id, name, status, lessonId, studentId, studentName);
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> createLessonGroup(@RequestBody LessonGroupDto lessonGroupDto)
            throws FieldRequiredException, EntityAlreadyExistException, EntityNotFoundException {
        return lessonGroupService.createLessonGroup(lessonGroupDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateLessonGroup(
            @PathVariable(value = "id") Long id,
            @RequestBody UpdateLessonGroupDto lessonGroupDto
    )
            throws EntityNotFoundException, EntityAlreadyExistException, FieldRequiredException {
        return lessonGroupService.updateLessonGroup(id, lessonGroupDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> deleteLessonGroup(
            @PathVariable(value = "id") Long id
    )
            throws EntityNotFoundException, EntityUsedException {
        return lessonGroupService.deleteLessonGroup(id);
    }
}
