package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.RequestStudentDto;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadEnumException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.FieldNotNullException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.FieldRequiredException;
import com.defiigosProject.SchoolCRMBackend.service.RequestStudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requestStudents")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RequestStudentController {

    private final RequestStudentService requestStudentService;

    public RequestStudentController(RequestStudentService requestStudentService) {
        this.requestStudentService = requestStudentService;
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> createRequestStudent(
            @RequestBody RequestStudentDto requestStudentDto)
            throws FieldRequiredException, EntityNotFoundException {
        return requestStudentService.createRequestStudent(requestStudentDto);
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getRequestStudent(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "locationId", required = false) Long locationId
    )
            throws BadEnumException {
        return requestStudentService.getRequestStudent(id, name, phone, status, locationId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> updateRequestStudent(
            @PathVariable(value = "id") Long id,
            @RequestBody RequestStudentDto requestStudentDto
    )
            throws EntityNotFoundException, FieldRequiredException, FieldNotNullException {
        return requestStudentService.updateRequestStudent(id, requestStudentDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteRequestStudent(
            @PathVariable(value = "id") Long id)
            throws EntityNotFoundException {
        return requestStudentService.deleteRequestStudent(id);
    }
}
