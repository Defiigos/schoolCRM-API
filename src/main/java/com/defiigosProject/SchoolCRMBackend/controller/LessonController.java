package com.defiigosProject.SchoolCRMBackend.controller;


import com.defiigosProject.SchoolCRMBackend.dto.lesson.LessonCreateDto;
import com.defiigosProject.SchoolCRMBackend.dto.lesson.LessonDto;
import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadEnumException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.FieldRequiredException;
import com.defiigosProject.SchoolCRMBackend.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping()
    public ResponseEntity<List<LessonDto>> getLesson(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "teacherId", required = false) Long teacherId,
            @RequestParam(value = "durationId", required = false) Long durationId,
            @RequestParam(value = "locationId", required = false) Long locationId,
            @RequestParam(value = "lessonGroup", required = false) Long lessonGroupId,
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "timeFrom", required = false) String timeFrom,
            @RequestParam(value = "timeTo", required = false) String timeTo
    )
            throws BadEnumException {
        return lessonService.getLesson(id, date, time, status,
                teacherId, durationId, locationId, lessonGroupId,
                dateFrom, dateTo, timeFrom, timeTo);
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> createLesson(@RequestBody LessonCreateDto request)
            throws FieldRequiredException, EntityNotFoundException, IllegalAccessException, BadRequestException {
        return lessonService.createLesson(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateLesson(
            @PathVariable(value = "id") Long id,
            @RequestBody LessonDto lessonDto
    )
            throws EntityNotFoundException, FieldRequiredException, BadRequestException {
        return lessonService.updateLesson(id, lessonDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteLesson(@PathVariable(value = "id") Long id)
            throws EntityNotFoundException, BadRequestException {
        return lessonService.deleteLesson(id);
    }
}
