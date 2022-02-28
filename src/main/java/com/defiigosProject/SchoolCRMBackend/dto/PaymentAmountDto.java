package com.defiigosProject.SchoolCRMBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentAmountDto {
    private Long id;
    private Float sum;
    private String name;
}
