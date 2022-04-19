package com.defiigosProject.SchoolCRMBackend.dto.user;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateFullUserDto {
    private String username;
    private String email;
    private String password;
    private RoleType roles;
    private Boolean active;
}
