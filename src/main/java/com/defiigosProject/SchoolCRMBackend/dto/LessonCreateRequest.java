package com.defiigosProject.SchoolCRMBackend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LessonCreateRequest {
    private LocalDate date;
    private LocalTime time;
    private LessonDurationDto lessonDuration;
    private LocationDto location;
    private TeacherDto teacher;
    private LessonGroupDto lessonGroup;
    private PaymentAmountDto paymentAmount;
}
