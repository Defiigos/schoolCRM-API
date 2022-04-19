package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.payment.PaymentDto;
import com.defiigosProject.SchoolCRMBackend.dto.payment.PaymentUpdateDto;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadEnumException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.FieldRequiredException;
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

    @GetMapping()
    public ResponseEntity<List<PaymentDto>> getPayment(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "lessonId", required = false) Long lessonId,
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "teacherId", required = false) Long teacherId,
            @RequestParam(value = "amountId", required = false) Long amountId,
            @RequestParam(value = "studentName", required = false) String studentName,
            @RequestParam(value = "teacherName", required = false) String teacherName,
            @RequestParam(value = "amountName", required = false) String amountName,
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "timeFrom", required = false) String timeFrom,
            @RequestParam(value = "timeTo", required = false) String timeTo
    )
            throws BadEnumException {
        return paymentService.getPayment(id, lessonId, studentId, teacherId, amountId,
                date, time, status, studentName, teacherName, amountName, dateFrom, dateTo, timeFrom, timeTo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updatePayment(
            @PathVariable(value = "id") Long id,
            @RequestBody PaymentUpdateDto request
    )
            throws EntityNotFoundException, FieldRequiredException {
        return paymentService.updatePayment(id, request);
    }
}
