package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.LocationDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.model.LocationStatus;
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

    public ResponseEntity<MessageResponse> createLocation(LocationDto locationDto)
            throws FieldRequiredException, EntityAlreadyExistException {
        if (locationDto.getAddress() == null || locationDto.getAddress().isEmpty()){
            throw new FieldRequiredException("address");
        }

        if (locationRepo.existsByAddress(locationDto.getAddress())){
            throw new EntityAlreadyExistException("location");
        }

        Location newLocation = new Location(locationDto.getAddress(), locationDto.getName());
        LocationStatus newStatus = locationStatusRepo.findByStatus(LocationStatusType.LOCATION_ACTIVE)
                .orElseThrow(() -> new RuntimeException("Error, Location status "
                        + LocationStatusType.LOCATION_ACTIVE + " is not found"));
        newStatus.addLocation(newLocation);

        locationRepo.save(newLocation);
        return ResponseEntity.ok(new MessageResponse("Location successfully created"));
    }

    public ResponseEntity<List<LocationDto>> getLocation(Long id, String address, String name, LocationStatusType status){
        List<Location> locationList = locationRepo.findAll(
                where(withId(id))
                        .and(withAddress(address))
                        .and(withName(name))
                        .and(withStatus(status))
        );

        List<LocationDto> locationDtoList = new ArrayList<>();
        for (Location location: locationList) {
            locationDtoList.add(new LocationDto(
                    location.getId(),
                    location.getAddress(),
                    location.getName(),
                    location.getStatus().getStatus()
            ));
        }

        return ResponseEntity.ok(locationDtoList);
    }

    public ResponseEntity<MessageResponse> updateLocation(Long id, LocationDto locationDto)
            throws EntityNotFoundException, FieldNotNullException {
        Optional<Location> optionalLocation = locationRepo.findById(id);
        if (optionalLocation.isEmpty())
            throw new EntityNotFoundException("location with this id:" + id);
        Location findLocation = optionalLocation.get();

        if (locationDto.getAddress() != null){
            if (locationDto.getAddress().isEmpty())
                throw new FieldNotNullException("address");
            findLocation.setAddress(locationDto.getAddress());
        }

        if (locationDto.getName() != null){
            findLocation.setName(locationDto.getName());
        }

        if (locationDto.getStatus() != null) {
            LocationStatus oldStatus = locationStatusRepo
                    .findByStatus(findLocation.getStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old location status is not found"));
            oldStatus.removeLocation(findLocation);

            LocationStatus newStatus = locationStatusRepo
                    .findByStatus(locationDto.getStatus())
                    .orElseThrow(() -> new EntityNotFoundException("location status"));
            newStatus.addLocation(findLocation);
        }

        locationRepo.save(findLocation);
        return ResponseEntity.ok(new MessageResponse("Location successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteLocation(Long id) throws EntityNotFoundException, EntityUsedException {
        Optional<Location> optionalLocation = locationRepo.findById(id);

        if (optionalLocation.isEmpty())
            throw new EntityNotFoundException("location with this id:" + id);
        Location deletedLocation = optionalLocation.get();

        if (!deletedLocation.getRequestStudentList().isEmpty())
            throw new EntityUsedException("location", "request students");

        if (!deletedLocation.getLessonList().isEmpty())
            throw new EntityUsedException("location", "lessons");

        locationRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Location successfully deleted"));
    }
}
