package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestStudentDto {
    private Long id;
    private String name;
    private String phone;
    private RequestStudentStatusType status;
    private LocationDto location;
}
