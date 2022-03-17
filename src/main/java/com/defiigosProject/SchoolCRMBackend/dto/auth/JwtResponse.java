package com.defiigosProject.SchoolCRMBackend.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String username;
    private List<String> roles;

    public JwtResponse(String token, Long id, String email, String username, List<String> roles) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }
}
