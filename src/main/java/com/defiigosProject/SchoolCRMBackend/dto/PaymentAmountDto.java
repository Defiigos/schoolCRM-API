package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.PaymentAmount;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentAmountDto {
    private Long id;
    private Float sum;
    private String name;

    public static PaymentAmountDto build (PaymentAmount paymentAmount) {
        return new PaymentAmountDto(
                paymentAmount.getId(),
                paymentAmount.getSum(),
                paymentAmount.getName()
        );
    }
}
