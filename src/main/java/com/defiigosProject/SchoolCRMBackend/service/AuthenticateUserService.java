package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.UserDto;
import com.defiigosProject.SchoolCRMBackend.dto.JwtResponse;
import com.defiigosProject.SchoolCRMBackend.dto.LoginDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.BadRequestException;
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

    public ResponseEntity authenticateUser(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword()));

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

    public ResponseEntity<MessageResponse> createUser(UserDto userDto) throws BadRequestException{
        String createUserEmail = userDto.getEmail();
        String createUserPassword = userDto.getPassword();

        if (createUserEmail == null || createUserEmail.isEmpty()){
            throw new BadRequestException("Error: Email('email') required");
        }

        if (createUserPassword == null || createUserPassword.isEmpty()){
            throw new BadRequestException("Error: Password('password') required");
        }

        if (userRepo.existsByEmail(createUserEmail)){
            throw new BadRequestException("Error: This email is already register");
        }

        User user = new User(
                userDto.getUsername(),
                createUserEmail,
                passwordEncoder.encode(createUserPassword)
        );

        Set<String> requestRoles = userDto.getRoles();
        Set<Role> roles = new HashSet<>();

        if (requestRoles == null) {
            Role userRole = roleRepo.findByName(RoleType.ROLE_USER).orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            for (String role: requestRoles)
            {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepo.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                        roles.add(adminRole);
                        break;

                    case "ROLE_USER":
                        Role userRole = roleRepo.findByName(RoleType.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
                        roles.add(userRole);
                        break;

                    default:
                        throw new BadRequestException("Error, Role " + role + " is not found");
                }
            }
        }

        for (Role role: roles) {
            user.addRole(role);
        }
        userRepo.save(user);
        return ResponseEntity.ok(new MessageResponse("User successfully created"));
    }
}
