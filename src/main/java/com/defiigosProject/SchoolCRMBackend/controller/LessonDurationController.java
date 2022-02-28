package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.LessonDurationDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.service.LessonDurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/lessons/durations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonDurationController {

    private final LessonDurationService lessonDurationService;

    public LessonDurationController(LessonDurationService lessonDurationService) {
        this.lessonDurationService = lessonDurationService;
    }

//    TODO авторизация @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<List<LessonDurationDto>> getLessonDuration(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "name", required = false) String name
    ) throws BadRequestException {
        try {
            if (time != null)
                return lessonDurationService.getLessonDuration(id, LocalTime.parse(time), name);
            else
                return lessonDurationService.getLessonDuration(id, null, name);
        }
        catch (DateTimeParseException e) {
            throw new BadRequestException("Time('time') format incorrect");
        }
    }

//    TODO авторизация @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createLessonDuration(
            @RequestBody LessonDurationDto lessonDurationDto)
            throws FieldRequiredException, EntityAlreadyExistException {
        return lessonDurationService.createLessonDuration(lessonDurationDto);
    }

//    TODO авторизация @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateLessonDuration(
            @PathVariable(value = "id") Long id,
            @RequestBody LessonDurationDto lessonDurationDto
    ) throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException {
        return lessonDurationService.updateLessonDuration(id, lessonDurationDto);
    }

//    TODO авторизация hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteLessonDuration(
            @PathVariable(value = "id") Long id) throws EntityNotFoundException, EntityUsedException {
        return lessonDurationService.deleteLessonDuration(id);
    }
}
