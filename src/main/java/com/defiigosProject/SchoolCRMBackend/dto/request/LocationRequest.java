package com.defiigosProject.SchoolCRMBackend.dto.request;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType;
import lombok.Data;

@Data
public class LocationRequest {
    private String address;
    private String name;
    private LocationStatusType status;
}
