package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.request.CreateUserRequest;
import com.defiigosProject.SchoolCRMBackend.dto.response.JwtResponse;
import com.defiigosProject.SchoolCRMBackend.dto.request.LoginRequest;
import com.defiigosProject.SchoolCRMBackend.dto.response.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.model.Role;
import com.defiigosProject.SchoolCRMBackend.model.User;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import com.defiigosProject.SchoolCRMBackend.repo.RoleRepo;
import com.defiigosProject.SchoolCRMBackend.repo.UserRepo;
import com.defiigosProject.SchoolCRMBackend.security.jwt.JwtUtils;
import com.defiigosProject.SchoolCRMBackend.service.impl.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticateUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateUserService(AuthenticationManager authenticationManager,
                                   JwtUtils jwtUtils,
                                   UserRepo userRepo,
                                   RoleRepo roleRepo,
                                   PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToke(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getUsername(),
                roles));
    }

    public ResponseEntity<MessageResponse> createUser(CreateUserRequest createUserRequest){
        String createUserEmail = createUserRequest.getEmail();

        if (createUserEmail == null || createUserEmail.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email required"));
        }

        if (userRepo.existsByEmail(createUserEmail)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This email is already register"));
        }

        User user = new User(
                createUserEmail,
                createUserRequest.getUsername(),
                passwordEncoder.encode(createUserRequest.getPassword())
        );

        Set<String> requestRoles = createUserRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (requestRoles == null) {
            Role userRole = roleRepo.findByName(RoleType.ROLE_USER).orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            requestRoles.forEach(role -> {
                switch (role){
                    case "ADMIN":
                        Role adminRole = roleRepo.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepo.findByName(RoleType.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("User successfully created"));
    }
}