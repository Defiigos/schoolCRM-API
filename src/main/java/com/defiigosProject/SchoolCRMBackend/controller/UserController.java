package com.defiigosProject.SchoolCRMBackend.controller;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.user.*;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<List<UserDto>> getUser(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "roles", required = false) String role,
            @RequestParam(value = "active", required = false) Boolean active
    )
            throws BadEnumException {
        return userService.getUser(id, name, email, role, active);
    }

    @PostMapping()
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> createUser(@RequestBody CreateUserDto userDto)
            throws BadRequestException, FieldRequiredException, EntityAlreadyExistException, BadEnumException {
        return userService.createUser(userDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> deleteUser(
            @PathVariable(value = "id") Long id
    )
            throws EntityNotFoundException, EntityUsedException {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}/active")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> updateUserActive(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "active") Boolean active
    )
            throws BadRequestException, EntityNotFoundException {
        return userService.updateUserActive(id, active);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> updateUserRole(
            @PathVariable(value = "id") Long id,
            @RequestBody UpdateUserRoleDto roleDto
    )
            throws BadRequestException, EntityNotFoundException {
        return userService.updateUserRole(id, roleDto);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<MessageResponse> updateUserPassword(
            @PathVariable(value = "id") Long id,
            @RequestBody UpdateUserPasswordDto passDto
    )
            throws EntityNotFoundException, FieldRequiredException, BadRequestException {
        return userService.updateUserPassword(id, passDto);
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<MessageResponse> updateUserEmail(
            @PathVariable(value = "id") Long id,
            @RequestBody UpdateUserEmailDto emailDto
    )
            throws EntityNotFoundException, EntityAlreadyExistException, FieldRequiredException, BadRequestException {
        return userService.updateUserEmail(id, emailDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(
            @PathVariable(value = "id") Long id,
            @RequestBody UpdateUserDto userDto
    )
            throws EntityNotFoundException, BadRequestException {
        return userService.updateUser(id, userDto);
    }

    @PutMapping("/{id}/full")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<MessageResponse> updateFullUser(
            @PathVariable(value = "id") Long id,
            @RequestBody UpdateFullUserDto userDto
    )
            throws EntityNotFoundException {
        return userService.updateFullUser(id, userDto);
    }
}
