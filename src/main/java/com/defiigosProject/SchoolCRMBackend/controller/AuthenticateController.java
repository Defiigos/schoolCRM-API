package com.defiigosProject.SchoolCRMBackend.controller;


import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.service.AuthenticateUserService;
import com.defiigosProject.SchoolCRMBackend.dto.auth.LoginRequest;
import com.defiigosProject.SchoolCRMBackend.dto.auth.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticateController {

    private final AuthenticateUserService authenticationUserService;

    public AuthenticateController(AuthenticateUserService authenticationUserService) {
        this.authenticationUserService = authenticationUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authUser(@RequestBody LoginRequest loginRequest) {
        return authenticationUserService.authenticateUser(loginRequest);
    }

    @GetMapping("/check")
    public ResponseEntity<MessageResponse> checkUser() {
        return ResponseEntity.ok(new MessageResponse("User token is valid!"));
    }
}
