package com.defiigosProject.SchoolCRMBackend.dto.user;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateUserRoleDto {
    private Set<RoleType> roles;
}

