package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class PaymentUpdateRequest {
    private LocalDate payDate;
    private LocalTime payTime;
    private PaymentAmountDto amountDto;
    private PaymentStatusType status;
}
