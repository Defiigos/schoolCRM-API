package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDto {
    private Long id;
    private String address;
    private String name;
    private LocationStatusType status;
}
