package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.payment.PaymentAmountDto;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.service.PaymentAmountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments/amounts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentAmountController {

    private final PaymentAmountService paymentAmountService;

    public PaymentAmountController(PaymentAmountService paymentAmountService) {
        this.paymentAmountService = paymentAmountService;
    }

    @GetMapping()
    public ResponseEntity<List<PaymentAmountDto>> getPaymentAmount(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "sum", required = false) String sum,
            @RequestParam(value = "name", required = false) String name
    ) throws BadRequestException {
        return paymentAmountService.getPaymentAmount(id, sum, name);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> createPaymentAmount(@RequestBody PaymentAmountDto paymentAmountDto)
            throws FieldRequiredException, EntityAlreadyExistException {
        return paymentAmountService.createPaymentAmount(paymentAmountDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> updatePaymentAmount(
            @PathVariable(value = "id") Long id,
            @RequestBody PaymentAmountDto paymentAmountDto
    ) throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException, EntityUsedException {
        return paymentAmountService.updatePaymentAmount(id, paymentAmountDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> deletePaymentAmount(@PathVariable(value = "id") Long id)
            throws EntityNotFoundException, EntityUsedException {
        return paymentAmountService.deletePaymentAmount(id);
    }
}
