package com.defiigosProject.SchoolCRMBackend.dto.response;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestStudentResponse {
    private Long id;
    private String name;
    private String phone;
    private RequestStudentStatusType statusType;
    private LocationResponse location;
}
