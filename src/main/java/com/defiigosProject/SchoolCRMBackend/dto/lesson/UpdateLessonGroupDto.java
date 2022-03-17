package com.defiigosProject.SchoolCRMBackend.dto.lesson;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import lombok.Data;

@Data
public class UpdateLessonGroupDto {
    private Long id;
    private String name;
    private LessonGroupStatusType status;
}
