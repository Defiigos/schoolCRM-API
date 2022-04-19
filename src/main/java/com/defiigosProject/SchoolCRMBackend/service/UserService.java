package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.user.*;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.model.Role;
import com.defiigosProject.SchoolCRMBackend.model.User;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import com.defiigosProject.SchoolCRMBackend.repo.RoleRepo;
import com.defiigosProject.SchoolCRMBackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

import static com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType.*;
import static com.defiigosProject.SchoolCRMBackend.repo.Specification.UserSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class UserService {

    private final String uri;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(@Value("${URI}") String uri, UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.uri = uri;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<MessageResponse> createUser(CreateUserDto userDto)
            throws BadRequestException, FieldRequiredException, EntityAlreadyExistException, BadEnumException {

        String createUserEmail = userDto.getEmail();
        String createUserPassword = userDto.getPassword();

        if (createUserEmail == null || createUserEmail.isEmpty()){
            throw new FieldRequiredException("email");
        }

        if (createUserPassword == null || createUserPassword.isEmpty()){
            throw new FieldRequiredException("password");
        }

        if (userRepo.existsByEmail(createUserEmail)){
            throw new EntityAlreadyExistException("email");
        }

        Set<RoleType> paresRole = new HashSet<>();
        try {
            if (userDto.getRoles() == null)
                throw new FieldRequiredException("roles");
            else {
                String[] rowRoles = userDto.getRoles().split(", ");
                for (String role: rowRoles) {
                    System.out.println(role);
                    paresRole.add(RoleType.valueOf(role));
                }
            }
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(RoleType.class, userDto.getRoles());
        }

        User user = new User(
                userDto.getUsername(),
                createUserEmail,
                passwordEncoder.encode(createUserPassword),
                true
        );


        Set<Role> roles = new HashSet<>();
        if (paresRole.isEmpty()) {
            Role userRole = roleRepo.findByName(ROLE_USER).
                    orElseThrow(() -> new RuntimeException("Error, Role " + ROLE_USER + " is not found"));
            roles.add(userRole);
        } else {
            for (RoleType role: paresRole)
            {
                switch (role) {
                    case ROLE_ADMIN:
                        Role adminRole = roleRepo.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error, Role " + ROLE_ADMIN + " is not found"));
                        roles.add(adminRole);
                        break;

                    case ROLE_USER:
                        Role userRole = roleRepo.findByName(ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error, Role " + ROLE_USER + " is not found"));
                        roles.add(userRole);
                        break;

                    case ROLE_SUPERADMIN:
                        Role superRole = roleRepo.findByName(ROLE_SUPERADMIN)
                                .orElseThrow(() -> new RuntimeException("Error, Role " + ROLE_SUPERADMIN + " is not found"));
                        roles.add(superRole);
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
        return ResponseEntity.created(URI.create(uri + "/api/users"))
                .body(new MessageResponse("User successfully created"));
    }

    public ResponseEntity<List<UserDto>> getUser(Long id, String name, String email, String role, Boolean active)
            throws BadEnumException {

        RoleType paresRole = null;
        try {
            if (role != null) {
                    paresRole = RoleType.valueOf(role);
            }
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(RoleType.class, role);
        }

        List<User> userList = userRepo.findAll(
                where(withId(id))
                        .and(withName(name))
                        .and(withEmail(email))
                        .and(hasRole(paresRole))
                        .and(withActive(active)),
                Sort.by(Sort.Direction.ASC, "id")
        );

        List<UserDto> userDtoList = new ArrayList<>();
        for (User user: userList) {
            userDtoList.add(UserDto.build(user));
        }

        return ResponseEntity.ok(userDtoList);
    }

    public ResponseEntity<MessageResponse> updateFullUser(Long id, UpdateFullUserDto userDto)
            throws EntityNotFoundException {

        User updatedUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with this id:" + id));

        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()){
            if (!userDto.getUsername().equals(updatedUser.getUsername())) {
                updatedUser.setUsername(userDto.getUsername());
            }
        }

        if (userDto.getActive() != null) {
            if (updatedUser.getIsActive() != userDto.getActive())
                updatedUser.setIsActive(userDto.getActive());
        }

        if (userDto.getRoles() != null) {
            Set<Role> roles = new HashSet<>(updatedUser.getRoles());
            for (Role role: roles) {
                updatedUser.removeRole(role);
            }

            Role role = roleRepo.findByName(userDto.getRoles())
                    .orElseThrow(() -> new EntityNotFoundException("role"));
            updatedUser.addRole(role);
        }

        if (userDto.getEmail() != null) {
            if (!updatedUser.getEmail().equals(userDto.getEmail())) {
                if (!userRepo.existsByEmail(userDto.getEmail())) {
                    updatedUser.setEmail(userDto.getEmail());
                }
            }
        }



        userRepo.save(updatedUser);
        return ResponseEntity.ok(new MessageResponse("User successfully updated"));
    }

    public ResponseEntity<MessageResponse> updateUser(Long id, UpdateUserDto userDto)
            throws EntityNotFoundException, BadRequestException {

        User updatedUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with this id:" + id));

        if (!updatedUser.getIsActive())
            throw new BadRequestException("can not modify blocked user!");

        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()){
            if (userDto.getUsername().equals(updatedUser.getUsername())) {
                throw new BadRequestException("this is same name!");
            }
            updatedUser.setUsername(userDto.getUsername());
        }

        userRepo.save(updatedUser);
        return ResponseEntity.ok(new MessageResponse("User successfully updated"));
    }

    public ResponseEntity<MessageResponse> updateUserEmail(Long id, UpdateUserEmailDto emailDto)
            throws EntityNotFoundException, EntityAlreadyExistException, FieldRequiredException, BadRequestException {

        if (emailDto.getNewEmail() == null || emailDto.getNewEmail().isEmpty()){
            throw new FieldRequiredException("new email");
        }

        if (emailDto.getPassword() == null || emailDto.getPassword().isEmpty()){
            throw new FieldRequiredException("password");
        }

        User updatedUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with this id:" + id));

        if (!updatedUser.getIsActive())
            throw new BadRequestException("can not modify blocked user!");

        if (updatedUser.getEmail().equals(emailDto.getNewEmail())) {
            throw new BadRequestException("this is same email!");
        }

        if (userRepo.existsByEmail(emailDto.getNewEmail())) {
            throw new EntityAlreadyExistException("email");
        }

        if (passwordEncoder.matches(emailDto.getPassword(), updatedUser.getPassword())) {
            updatedUser.setEmail(emailDto.getNewEmail());
        } else {
            throw new BadRequestException("password incorrect!");
        }

        userRepo.save(updatedUser);
        return ResponseEntity.ok(new MessageResponse("User email successfully updated"));
    }

    public ResponseEntity<MessageResponse> updateUserPassword(Long id, UpdateUserPasswordDto passDto)
            throws EntityNotFoundException, FieldRequiredException, BadRequestException {

        if (passDto.getOldPassword() == null || passDto.getOldPassword().isEmpty()) {
            throw new FieldRequiredException("old password");
        }

        if (passDto.getNewPassword() == null || passDto.getNewPassword().isEmpty()) {
            throw new FieldRequiredException("new password");
        }

        User updatedUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with this id:" + id));

        if (!updatedUser.getIsActive())
            throw new BadRequestException("can not modify blocked user!");

        if (passwordEncoder.matches(passDto.getOldPassword(), updatedUser.getPassword())) {
            if (passwordEncoder.matches(passDto.getNewPassword(), updatedUser.getPassword()))
                throw new BadRequestException("this is same password!");
            updatedUser.setPassword(passwordEncoder.encode(passDto.getNewPassword()));
        } else {
            throw new BadRequestException("old password incorrect!");
        }

        userRepo.save(updatedUser);
        return ResponseEntity.ok(new MessageResponse("User password successfully updated"));
    }

    public ResponseEntity<MessageResponse> updateUserActive(Long id, Boolean isActive)
            throws EntityNotFoundException, BadRequestException {

        User updatedUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with this id:" + id));

        if (updatedUser.getIsActive().equals(isActive))
            throw new BadRequestException("this is same active!");

        updatedUser.setIsActive(isActive);

        userRepo.save(updatedUser);
        return ResponseEntity.ok(new MessageResponse("User active successfully updated"));
    }

    public ResponseEntity<MessageResponse> updateUserRole(Long id, UpdateUserRoleDto roleDto)
            throws EntityNotFoundException, BadRequestException {

        if (roleDto.getRoles() == null || roleDto.getRoles().isEmpty()) {
            throw new BadRequestException("role list required");
        }

        User updatedUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with this id:" + id));

        Set<Role> roles = new HashSet<>(updatedUser.getRoles());
        for (Role role: roles) {
            updatedUser.removeRole(role);
        }

        for (RoleType roleType: roleDto.getRoles()){
            Role role = roleRepo.findByName(roleType)
                    .orElseThrow(() -> new EntityNotFoundException("role"));
            updatedUser.addRole(role);
        }

        userRepo.save(updatedUser);
        return ResponseEntity.ok(new MessageResponse("User role successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteUser(Long id)
            throws EntityNotFoundException, EntityUsedException {

        User deletedUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with this id:" + id));

        if (!deletedUser.getLessons().isEmpty())
            throw new EntityUsedException("user", "lesson");

        Set<Role> roles = new HashSet<>(deletedUser.getRoles());
        for (Role role: roles) {
            deletedUser.removeRole(role);
        }

        userRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("User successfully deleted"));
    }
}
