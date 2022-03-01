package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherDto {
    private Long id;
    private String name;
    private String email;

    public static TeacherDto build(User user) {
        return new TeacherDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
