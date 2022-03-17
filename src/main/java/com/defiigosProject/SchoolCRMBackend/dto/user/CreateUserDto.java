package com.defiigosProject.SchoolCRMBackend.dto.user;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import lombok.Data;

import java.util.Set;

@Data
public class CreateUserDto {
    private String username;
    private String email;
    private String password;
    private Set<RoleType> roles;
}
