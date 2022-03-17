package com.defiigosProject.SchoolCRMBackend.dto.payment;

import com.defiigosProject.SchoolCRMBackend.dto.StudentDto;
import com.defiigosProject.SchoolCRMBackend.dto.lesson.LessonDto;
import com.defiigosProject.SchoolCRMBackend.model.Payment;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private LessonDto lessonDto;
    private StudentDto studentDto;
    private LocalDate payDate;
    private LocalTime payTime;
    private PaymentAmountDto amountDto;
    private PaymentStatusType status;

    public static PaymentDto build(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                LessonDto.build(payment.getLesson()),
                StudentDto.build(payment.getStudent()),
                payment.getPayDate(),
                payment.getPayTime(),
                PaymentAmountDto.build(payment.getAmount()),
                payment.getStatus().getStatus()
        );
    }
}
