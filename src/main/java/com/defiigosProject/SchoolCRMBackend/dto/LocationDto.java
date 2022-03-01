package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.Location;
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

    public static LocationDto build(Location location){
        return new LocationDto(
                location.getId(),
                location.getAddress(),
                location.getName(),
                location.getStatus().getStatus()
        );
    }
}
