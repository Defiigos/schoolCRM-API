package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.StudentStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private String phone;
    private String parentName;
    private String parentPhone;
    private String description;
    private StudentStatusType status;
}
