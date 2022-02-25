package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.service.RequestStudentService;
import com.defiigosProject.SchoolCRMBackend.dto.request.CreateRequestStudentRequest;
import com.defiigosProject.SchoolCRMBackend.dto.request.UpdateRequestStudentRequest;
import com.defiigosProject.SchoolCRMBackend.dto.response.MessageResponse;
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

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<?> getRequestStudent(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "locationId", required = false) Long locationId
    ){
        return requestStudentService.getRequestStudent(id, name, phone, status, locationId);
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createRequestStudent(
            @RequestBody CreateRequestStudentRequest createRequestStudentRequest){
        return requestStudentService.createRequestStudent(createRequestStudentRequest);
    }

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRequestStudent(
            @PathVariable(value = "id") Long id,
            @RequestBody UpdateRequestStudentRequest updateRequestStudentRequest
            ){
        return requestStudentService.updateRequestStudent(id, updateRequestStudentRequest);
    }
}
