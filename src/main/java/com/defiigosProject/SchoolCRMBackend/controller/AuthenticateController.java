package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.exception.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.service.AuthenticateUserService;
import com.defiigosProject.SchoolCRMBackend.dto.UserDto;
import com.defiigosProject.SchoolCRMBackend.dto.LoginDto;
import com.defiigosProject.SchoolCRMBackend.dto.JwtResponse;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
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

    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> authUser(@RequestBody LoginDto loginDto) {
        return authenticationUserService.authenticateUser(loginDto);
    }

    @PostMapping("/createUser")
//   TODO авторизация @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> createUser(@RequestBody UserDto userDto) throws BadRequestException {
       return authenticationUserService.createUser(userDto);
    }
}
