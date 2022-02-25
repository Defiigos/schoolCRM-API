package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.service.AuthenticateUserService;
import com.defiigosProject.SchoolCRMBackend.dto.request.CreateUserRequest;
import com.defiigosProject.SchoolCRMBackend.dto.request.LoginRequest;
import com.defiigosProject.SchoolCRMBackend.dto.response.JwtResponse;
import com.defiigosProject.SchoolCRMBackend.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticateController {

    private final AuthenticateUserService authenticationUserService;

    public AuthenticateController(AuthenticateUserService authenticationUserService) {
        this.authenticationUserService = authenticationUserService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> authUser(@RequestBody LoginRequest loginRequest) {
        return authenticationUserService.authenticateUser(loginRequest);
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> createUser(@RequestBody CreateUserRequest createUserRequest){
       return authenticationUserService.createUser(createUserRequest);
    }
}
