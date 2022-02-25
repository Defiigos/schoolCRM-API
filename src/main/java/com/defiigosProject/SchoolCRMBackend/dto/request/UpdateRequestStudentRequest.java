package com.defiigosProject.SchoolCRMBackend.dto.request;

import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import lombok.Data;

@Data
public class UpdateRequestStudentRequest {
    private String name;
    private String phone;
    private Location location;
    private RequestStudentStatusType status;
}
