package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.LocationDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping()
    public ResponseEntity<?> findAllLocation(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status
    ) {
        return locationService.getLocation(id, address, name, status);
    }

    //    TODO авторизация ? только так ? -> @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createLocation(@RequestBody LocationDto locationDto)
            throws BadRequestException {
        return locationService.createLocation(locationDto);
    }

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateLocation(
            @PathVariable(value = "id") Long id,
            @RequestBody LocationDto locationDto
    ) throws BadRequestException {
        return locationService.updateLocation(id, locationDto);
    }

//    TODO авторизация hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteLocation(
            @PathVariable(value = "id") Long id
    ) throws BadRequestException {
        return locationService.deleteLocation(id);
    }
}
