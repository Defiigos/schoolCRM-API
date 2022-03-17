package com.defiigosProject.SchoolCRMBackend.dto.lesson;

import com.defiigosProject.SchoolCRMBackend.dto.LocationDto;
import com.defiigosProject.SchoolCRMBackend.dto.payment.PaymentAmountDto;
import com.defiigosProject.SchoolCRMBackend.dto.TeacherDto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LessonCreateDto {
    private LocalDate date;
    private LocalTime time;
    private LessonDurationDto lessonDuration;
    private LocationDto location;
    private TeacherDto teacher;
    private LessonGroupDto lessonGroup;
    private PaymentAmountDto paymentAmount;
}
