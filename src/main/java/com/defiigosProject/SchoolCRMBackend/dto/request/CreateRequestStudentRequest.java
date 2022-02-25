package com.defiigosProject.SchoolCRMBackend.dto.request;

import com.defiigosProject.SchoolCRMBackend.model.Location;
import lombok.Data;

@Data
public class CreateRequestStudentRequest {
    private String name;
    private String phone;
    private Location location;
//    private Long locationId;
}
