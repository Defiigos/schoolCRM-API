package com.defiigosProject.SchoolCRMBackend.dto.user;

import lombok.Data;

@Data
public class UpdateUserPasswordDto {
    private String oldPassword;
    private String newPassword;
}
