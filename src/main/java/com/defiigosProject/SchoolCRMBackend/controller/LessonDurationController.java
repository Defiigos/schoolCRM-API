package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.lesson.LessonDurationDto;
import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.service.LessonDurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons/durations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonDurationController {

    private final LessonDurationService lessonDurationService;

    public LessonDurationController(LessonDurationService lessonDurationService) {
        this.lessonDurationService = lessonDurationService;
    }

    @GetMapping()
    public ResponseEntity<List<LessonDurationDto>> getLessonDuration(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "name", required = false) String name
    )
            throws BadRequestException {
        return lessonDurationService.getLessonDuration(id, time, name);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> createLessonDuration(
            @RequestBody LessonDurationDto lessonDurationDto)
            throws FieldRequiredException, EntityAlreadyExistException {
        return lessonDurationService.createLessonDuration(lessonDurationDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> updateLessonDuration(
            @PathVariable(value = "id") Long id,
            @RequestBody LessonDurationDto lessonDurationDto
    )
            throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException, EntityUsedException {
        return lessonDurationService.updateLessonDuration(id, lessonDurationDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteLessonDuration(@PathVariable(value = "id") Long id)
            throws EntityNotFoundException, EntityUsedException {
        return lessonDurationService.deleteLessonDuration(id);
    }
}
