package com.defiigosProject.SchoolCRMBackend.dto.payment;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentUpdateDto {
    private PaymentAmountDto amountDto;
    private PaymentStatusType status;
}
