package com.defiigosProject.SchoolCRMBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class LessonDurationDto {
    private Long id;
    private LocalTime time;
    private String name;
}
