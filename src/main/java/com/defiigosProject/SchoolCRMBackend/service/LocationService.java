package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.request.LocationRequest;
import com.defiigosProject.SchoolCRMBackend.dto.response.LocationResponse;
import com.defiigosProject.SchoolCRMBackend.dto.response.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.model.LocationStatus;
import com.defiigosProject.SchoolCRMBackend.model.RequestStudentStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.LocationRepo;
import com.defiigosProject.SchoolCRMBackend.repo.LocationStatusRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.defiigosProject.SchoolCRMBackend.repo.Specification.LocationSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class LocationService {

    private final LocationRepo locationRepo;
    private final LocationStatusRepo locationStatusRepo;

    public LocationService(LocationRepo locationRepo, LocationStatusRepo locationStatusRepo) {
        this.locationRepo = locationRepo;
        this.locationStatusRepo = locationStatusRepo;
    }

    public ResponseEntity<MessageResponse> createLocation(LocationRequest locationRequest) throws BadRequestException {
        if (locationRequest.getAddress() == null || locationRequest.getAddress().isEmpty()){
            throw new BadRequestException("Error: Address('address') required");
        }

        if (locationRepo.existsByAddress(locationRequest.getAddress())){
            throw new BadRequestException("Error: Location with tis Address('address') already exist");
        }

        Location newLocation = new Location(locationRequest.getAddress(), locationRequest.getName());
        LocationStatus newStatus = locationStatusRepo.findByStatus(LocationStatusType.LOCATION_ACTIVE)
                .orElseThrow(() -> new RuntimeException("Error, Location status (LOCATION_ACTIVE) is not found"));
        newStatus.addLocation(newLocation);

        locationRepo.save(newLocation);
        return ResponseEntity.ok(new MessageResponse("Location successfully created"));
    }

    public ResponseEntity<List<LocationResponse>> getLocation(Long id, String address, String name, String status){
        List<Location> locationList = locationRepo.findAll(
                where(withId(id))
                        .and(withAddress(address))
                        .and(withName(name))
                        .and(withStatus(status))
        );

        List<LocationResponse> locationResponseList = new ArrayList<>();
        for (Location location: locationList) {
            locationResponseList.add(new LocationResponse(
                    location.getId(),
                    location.getAddress(),
                    location.getName(),
                    location.getLocationStatus().getStatus()
            ));
        }

        return ResponseEntity.ok(locationResponseList);
    }

    public ResponseEntity<MessageResponse> updateLocation(Long id, LocationRequest locationRequest) throws BadRequestException{
        Optional<Location> optionalLocation = locationRepo.findById(id);
        if (optionalLocation.isEmpty())
            throw new BadRequestException("Error: Location with this id:" + id + "is not found");
        Location findLocation = optionalLocation.get();

        if (locationRequest.getAddress() != null){
            if (locationRequest.getAddress().isEmpty())
                throw new BadRequestException("Error: Address('address') must not be empty");
            findLocation.setAddress(locationRequest.getAddress());
        }

        if (locationRequest.getName() != null){
            findLocation.setName(locationRequest.getName());
        }

        if (locationRequest.getStatus() != null) {
            LocationStatus oldStatus = locationStatusRepo
                    .findByStatus(findLocation.getLocationStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old location status is not found"));
            oldStatus.removeLocation(findLocation);

            LocationStatus newStatus = locationStatusRepo
                    .findByStatus(locationRequest.getStatus())
                    .orElseThrow(() -> new BadRequestException("Error, New location status('status') is not found"));
            newStatus.addLocation(findLocation);
        }

        locationRepo.save(findLocation);
        return ResponseEntity.ok(new MessageResponse("Location successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteLocation(Long id) throws BadRequestException {
        Optional<Location> optionalLocation = locationRepo.findById(id);

        if (optionalLocation.isEmpty())
            throw new BadRequestException("Error: Location with this id:" + id + "is not found");
        Location deletedLocation = optionalLocation.get();

        if (!deletedLocation.getRequestStudentList().isEmpty())
            throw new BadRequestException("This location is used by request students");

        if (!deletedLocation.getLessonList().isEmpty())
            throw new BadRequestException("This location is used by lessons");

        locationRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Location successfully deleted"));
    }
}
