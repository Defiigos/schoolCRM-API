package com.defiigosProject.SchoolCRMBackend.controller;


import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.PaymentAmountDto;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.service.PaymentAmountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/payments/amounts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentAmountController {

    private final PaymentAmountService paymentAmountService;

    public PaymentAmountController(PaymentAmountService paymentAmountService) {
        this.paymentAmountService = paymentAmountService;
    }

    //    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<PaymentAmountDto>> getPaymentAmount(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "sum", required = false) String sum,
            @RequestParam(value = "name", required = false) String name
    ) throws BadRequestException {
        try {
            if (sum != null)
                return paymentAmountService.getPaymentAmount(id, Float.parseFloat(sum), name);
            else
                return paymentAmountService.getPaymentAmount(id, null, name);
        }
        catch (DateTimeParseException e) {
            throw new BadRequestException("sum format incorrect");
        }
    }

    //    TODO авторизация @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createPaymentAmount(
            @RequestBody PaymentAmountDto paymentAmountDto)
            throws FieldRequiredException, EntityAlreadyExistException {
        return paymentAmountService.createPaymentAmount(paymentAmountDto);
    }

    //    TODO авторизация @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updatePaymentAmount(
            @PathVariable(value = "id") Long id,
            @RequestBody PaymentAmountDto paymentAmountDto
    ) throws EntityNotFoundException, FieldNotNullException {
        return paymentAmountService.updatePaymentAmount(id, paymentAmountDto);
    }

    //    TODO авторизация hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletePaymentAmount(
            @PathVariable(value = "id") Long id) throws EntityNotFoundException, EntityUsedException {
        return paymentAmountService.deletePaymentAmount(id);
    }
}
