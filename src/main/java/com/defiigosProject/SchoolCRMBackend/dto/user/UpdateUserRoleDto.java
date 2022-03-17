package com.defiigosProject.SchoolCRMBackend.dto.user;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRoleDto {
    private Set<RoleType> roles;
}
