package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.RequestStudentDto;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import com.defiigosProject.SchoolCRMBackend.service.RequestStudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requestStudents")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RequestStudentController {

    private final RequestStudentService requestStudentService;

    public RequestStudentController(RequestStudentService requestStudentService) {
        this.requestStudentService = requestStudentService;
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createRequestStudent(
            @RequestBody RequestStudentDto requestStudentDto) throws FieldRequiredException, EntityNotFoundException {
        return requestStudentService.createRequestStudent(requestStudentDto);
    }

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<?> getRequestStudent(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "locationId", required = false) Long locationId
    ) throws BadEnumException {
        try {
            if (status != null)
                return requestStudentService.getRequestStudent(
                        id, name, phone, RequestStudentStatusType.valueOf(status), locationId);
            else
                return requestStudentService.getRequestStudent(id, name, phone, null, locationId);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(RequestStudentStatusType.class, status);
        }
    }

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRequestStudent(
            @PathVariable(value = "id") Long id,
            @RequestBody RequestStudentDto requestStudentDto
            ) throws EntityNotFoundException, FieldRequiredException, FieldNotNullException {
        return requestStudentService.updateRequestStudent(id, requestStudentDto);
    }

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRequestStudent(
            @PathVariable(value = "id") Long id) throws EntityNotFoundException {
        return requestStudentService.deleteRequestStudent(id);
    }
}
