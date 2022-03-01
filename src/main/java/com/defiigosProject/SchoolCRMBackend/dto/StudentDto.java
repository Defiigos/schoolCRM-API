package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.Student;
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

    public static StudentDto build(Student student) {
        return new StudentDto(
                student.getId(),
                student.getName(),
                student.getPhone(),
                student.getParentName(),
                student.getParentPhone(),
                student.getDescription(),
                student.getStatus().getStatus()
        );
    }
}
