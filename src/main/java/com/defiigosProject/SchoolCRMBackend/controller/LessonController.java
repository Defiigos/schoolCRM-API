package com.defiigosProject.SchoolCRMBackend.controller;


import com.defiigosProject.SchoolCRMBackend.dto.LessonCreateRequest;
import com.defiigosProject.SchoolCRMBackend.dto.LessonDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType;
import com.defiigosProject.SchoolCRMBackend.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

//    TODO авторизация ? только так ? -> @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<LessonDto>> getLesson(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "teacherId", required = false) Long teacherId,
            @RequestParam(value = "durationId", required = false) Long durationId,
            @RequestParam(value = "locationId", required = false) Long locationId
    ) throws BadEnumException {
        try {
            if (status != null)
                return lessonService.getLesson(id, date, time, LessonStatusType.valueOf(status), teacherId, durationId, locationId);
            else
                return lessonService.getLesson(id, date, time, null, teacherId, durationId, locationId);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(LessonGroupStatusType.class, status);
        }
    }

//    TODO авторизация ? только так ? -> @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createLesson(@RequestBody LessonCreateRequest request)
            throws FieldRequiredException, EntityNotFoundException, IllegalAccessException {
        return lessonService.createLesson(request);
    }

//    TODO авторизация ? только так ? -> @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateLesson(
            @PathVariable(value = "id") Long id,
            @RequestBody LessonDto lessonDto
    ) throws EntityNotFoundException, FieldRequiredException, BadRequestException {
        return lessonService.updateLesson(id, lessonDto);
    }
}
