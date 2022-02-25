package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.request.CreateLocationRequest;
import com.defiigosProject.SchoolCRMBackend.dto.response.MessageResponse;
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

    //    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createLocation(@RequestBody CreateLocationRequest locationRequest){
        return locationService.createLocation(locationRequest);
    }

    @GetMapping()
    public ResponseEntity<?> findAllLocation(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "name", required = false) String name
    ){
        return locationService.getLocation(id, address, name);
    }

    //@PutMapping(/{id})
}
