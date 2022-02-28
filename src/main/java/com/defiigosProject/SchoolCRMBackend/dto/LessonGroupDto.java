package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class LessonGroupDto {
    private Long id;
    private String name;
    private LessonGroupStatusType status;
    private Set<StudentDto> students;
}
