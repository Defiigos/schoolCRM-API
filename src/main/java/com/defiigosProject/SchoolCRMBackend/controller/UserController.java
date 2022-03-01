package com.defiigosProject.SchoolCRMBackend.controller;


import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.StudentDto;
import com.defiigosProject.SchoolCRMBackend.dto.UserDto;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import com.defiigosProject.SchoolCRMBackend.service.UserService;
import org.springframework.http.ResponseEntity;
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

//    TODO авторизация @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<UserDto>> getStudent(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "role", required = false) String role
    ) throws BadEnumException {
        try {
            if (role != null)
                return userService.getUser(id, name, email, RoleType.valueOf(role));
            else
                return userService.getUser(id, name, email, null);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(RoleType.class, role);
        }
    }

//    TODO авторизация @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createUser(@RequestBody UserDto userDto) throws BadRequestException {
        return userService.createUser(userDto);
    }

//    TODO авторизация @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(
            @PathVariable(value = "id") Long id,
            @RequestBody UserDto userDto
    ) throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException {
        return userService.updateUser(id, userDto);
    }

//    TODO авторизация @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(
            @PathVariable(value = "id") Long id
    ) throws EntityNotFoundException, EntityUsedException {
        return userService.deleteUser(id);
    }
}
