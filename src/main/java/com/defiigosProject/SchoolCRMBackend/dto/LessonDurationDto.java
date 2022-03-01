package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.LessonDuration;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class LessonDurationDto {
    private Long id;
    private LocalTime time;
    private String name;

    public static LessonDurationDto build(LessonDuration lessonDuration){
        return new LessonDurationDto(
                lessonDuration.getId(),
                lessonDuration.getTime(),
                lessonDuration.getName()
        );
    }
}
