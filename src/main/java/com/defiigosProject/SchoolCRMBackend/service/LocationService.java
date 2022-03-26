package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.LocationDto;
import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.model.LocationStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.LocationRepo;
import com.defiigosProject.SchoolCRMBackend.repo.LocationStatusRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType.LOCATION_ACTIVE;
import static com.defiigosProject.SchoolCRMBackend.repo.Specification.LocationSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class LocationService {

    private final String uri;
    private final LocationRepo locationRepo;
    private final LocationStatusRepo locationStatusRepo;

    public LocationService(@Value("${URI}") String uri, LocationRepo locationRepo, LocationStatusRepo locationStatusRepo) {
        this.uri = uri;
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

        LocationStatus newStatus = locationStatusRepo.findByStatus(LOCATION_ACTIVE)
                .orElseThrow(() -> new RuntimeException("Error, Location status "
                        + LOCATION_ACTIVE + " is not found"));

        Location newLocation = new Location(locationDto.getAddress(), locationDto.getName());
        newStatus.addLocation(newLocation);

        locationRepo.save(newLocation);
        return ResponseEntity.created(URI.create(uri + "/api/locations"))
                .body(new MessageResponse("Location successfully created"));
    }

    public ResponseEntity<List<LocationDto>> getLocation(Long id, String address, String name, String status)
            throws BadEnumException {

        LocationStatusType paresStatus = null;
        try {
            if (status != null)
                paresStatus = LocationStatusType.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(LocationStatusType.class, status);
        }

        List<Location> locationList = locationRepo.findAll(
                where(withId(id))
                        .and(withAddress(address))
                        .and(withName(name))
                        .and(withStatus(paresStatus)),
                Sort.by(Sort.Direction.ASC, "id")
        );

        List<LocationDto> locationDtoList = new ArrayList<>();
        for (Location location: locationList) {
            locationDtoList.add(LocationDto.build(location));
        }

        return ResponseEntity.ok(locationDtoList);
    }

    public ResponseEntity<MessageResponse> updateLocation(Long id, LocationDto locationDto)
            throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException, EntityUsedException {

        Location findLocation = locationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("location with this id:" + id));

        if (locationDto.getAddress() != null){
            if (locationDto.getAddress().isEmpty())
                throw new FieldNotNullException("address");

            if (!findLocation.getAddress().equals(locationDto.getAddress())) {
                if (locationRepo.existsByAddress(locationDto.getAddress())) {
                    throw new EntityAlreadyExistException("location");
                }

                if (!findLocation.getRequestStudents().isEmpty())
                    throw new EntityUsedException("location", "request students");

                if (!findLocation.getLessons().isEmpty()) {
                    throw new EntityUsedException("location", "lessons");
                }

                findLocation.setAddress(locationDto.getAddress());
            }
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

    public ResponseEntity<MessageResponse> deleteLocation(Long id)
            throws EntityNotFoundException, EntityUsedException {

        Location deletedLocation = locationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("location with this id:" + id));

        if (!deletedLocation.getRequestStudents().isEmpty())
            throw new EntityUsedException("location", "request students");

        if (!deletedLocation.getLessons().isEmpty())
            throw new EntityUsedException("location", "lessons");

        locationRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Location successfully deleted"));
    }
}
