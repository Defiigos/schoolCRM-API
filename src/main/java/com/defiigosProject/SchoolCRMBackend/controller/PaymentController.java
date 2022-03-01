package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.PaymentDto;
import com.defiigosProject.SchoolCRMBackend.dto.PaymentUpdateRequest;
import com.defiigosProject.SchoolCRMBackend.exception.BadEnumException;
import com.defiigosProject.SchoolCRMBackend.exception.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.FieldRequiredException;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import com.defiigosProject.SchoolCRMBackend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

//    TODO авторизация ? только так ? -> @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<PaymentDto>> getPayment(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "lessonId", required = false) Long lessonId,
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "amountId", required = false) Long amountId
    ) throws BadEnumException {
        try {
            if (status != null)
                return paymentService.getPayment(id, lessonId, studentId, amountId,
                        date, time, PaymentStatusType.valueOf(status));
            else
                return paymentService.getPayment(id, lessonId, studentId, amountId, date, time, null);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(LessonGroupStatusType.class, status);
        }
    }

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updatePayment(
            @PathVariable(value = "id") Long id,
            @RequestBody PaymentUpdateRequest request
    ) throws EntityNotFoundException, FieldRequiredException {
        return paymentService.updatePayment(id, request);
    }
}
