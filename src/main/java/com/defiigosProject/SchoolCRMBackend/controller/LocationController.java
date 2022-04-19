package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.LocationDto;
import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping()
    public ResponseEntity<List<LocationDto>> getLocation(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status
    )
            throws BadEnumException {
        return locationService.getLocation(id, address, name, status);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> createLocation(@RequestBody LocationDto locationDto)
            throws FieldRequiredException, EntityAlreadyExistException {
        return locationService.createLocation(locationDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> updateLocation(
            @PathVariable(value = "id") Long id,
            @RequestBody LocationDto locationDto
    )
            throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException, EntityUsedException {
        return locationService.updateLocation(id, locationDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> deleteLocation(@PathVariable(value = "id") Long id)
            throws EntityNotFoundException, EntityUsedException {
        return locationService.deleteLocation(id);
    }
}
