package com.defiigosProject.SchoolCRMBackend.dto.response;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponse {
    private Long id;
    private String address;
    private String name;
    private LocationStatusType status;
}
