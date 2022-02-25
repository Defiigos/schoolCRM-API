package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.request.CreateLocationRequest;
import com.defiigosProject.SchoolCRMBackend.dto.response.LocationResponse;
import com.defiigosProject.SchoolCRMBackend.dto.response.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.repo.LocationRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

    private final LocationRepo locationRepo;

    public LocationService(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public ResponseEntity<MessageResponse> createLocation(CreateLocationRequest locationRequest){
        if (locationRequest.getAddress() == null || locationRequest.getAddress().isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Address (\"address\") required"));
        }

        if (locationRequest.getName()!= null){

        }

        Location newLocation = new Location(locationRequest.getAddress(), locationRequest.getName());
        locationRepo.save(newLocation);
        return ResponseEntity.ok(new MessageResponse("Location successfully created"));
    }

    public ResponseEntity<List<LocationResponse>> getLocation(Long id, String address, String name){
        List<Location> locationList = locationRepo.findAll();
//                where(withId(id))
//                        .and(withAddress(phone))
//                        .and(withName(name))
//        );

        List<LocationResponse> locationResponseList = new ArrayList<>();
        for (Location location:
             locationList) {
            locationResponseList.add(new LocationResponse(
                    location.getId(),
                    location.getAddress(),
                    location.getName()
            ));
        }

        return ResponseEntity.ok(locationResponseList);
    }
}
