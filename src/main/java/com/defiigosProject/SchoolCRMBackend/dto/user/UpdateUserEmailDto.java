package com.defiigosProject.SchoolCRMBackend.dto.user;

import lombok.Data;

@Data
public class UpdateUserEmailDto {
    private String newEmail;
    private String password;
}
