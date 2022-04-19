package com.defiigosProject.SchoolCRMBackend.dto.user;

import com.defiigosProject.SchoolCRMBackend.model.Role;
import com.defiigosProject.SchoolCRMBackend.model.User;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Set<RoleType> roles;
    private Boolean active;

    public static UserDto build(User user) {
        Set<RoleType> roles = new HashSet<>();
        for (Role role: user.getRoles()){
            roles.add(role.getName());
        }

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles,
                user.getIsActive()
        );
    }
}
