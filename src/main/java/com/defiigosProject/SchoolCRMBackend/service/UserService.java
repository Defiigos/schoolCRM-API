package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.UserDto;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.model.Role;
import com.defiigosProject.SchoolCRMBackend.model.User;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import com.defiigosProject.SchoolCRMBackend.repo.RoleRepo;
import com.defiigosProject.SchoolCRMBackend.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.defiigosProject.SchoolCRMBackend.repo.Specification.UserSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<MessageResponse> createUser(UserDto userDto) throws BadRequestException {
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

        Set<RoleType> requestRoles = userDto.getRoles();
        Set<Role> roles = new HashSet<>();

        if (requestRoles == null) {
            Role userRole = roleRepo.findByName(RoleType.ROLE_USER).orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            for (RoleType role: requestRoles)
            {
                switch (role) {
                    case ROLE_ADMIN:
                        Role adminRole = roleRepo.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                        roles.add(adminRole);
                        break;

                    case ROLE_USER:
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

    public ResponseEntity<List<UserDto>> getUser(Long id, String name, String email, RoleType role) {

        List<User> userList = userRepo.findAll(
                where(withId(id))
                        .and(withName(name))
                        .and(withEmail(email))
                        .and(withRole(role))
        );

        List<UserDto> userDtoList = new ArrayList<>();
        for (User user: userList) {
            userDtoList.add(UserDto.build(user));
        }

        return ResponseEntity.ok(userDtoList);
    }

    public ResponseEntity<MessageResponse> updateUser(Long id, UserDto userDto)
            throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException {

        User updatedUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with this id:" + id));

        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()){
            updatedUser.setUsername(userDto.getUsername());
        }

        if (updatedUser.getEmail() != null){
            if (updatedUser.getEmail().isEmpty())
                throw new FieldNotNullException("email");

            if (!updatedUser.getEmail().equals(userDto.getEmail())) {
                if (userRepo.existsByEmail(userDto.getEmail())) {
                    throw new EntityAlreadyExistException("email");
                }
                updatedUser.setEmail(updatedUser.getEmail());
            }
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if (userDto.getRoles() != null) {
            for (Role role: updatedUser.getRoles()){
                updatedUser.removeRole(role);
            }

            for (RoleType roleType: userDto.getRoles()){
                Role role = roleRepo.findByName(roleType)
                        .orElseThrow(() -> new EntityNotFoundException("role"));
                updatedUser.addRole(role);
            }
        }

        userRepo.save(updatedUser);
        return ResponseEntity.ok(new MessageResponse("User successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteUser(Long id) throws EntityNotFoundException, EntityUsedException {

        User deletedUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with this id:" + id));

        if (!deletedUser.getLessons().isEmpty())
            throw new EntityUsedException("user", "lesson");

        userRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("User successfully deleted"));
    }
}
