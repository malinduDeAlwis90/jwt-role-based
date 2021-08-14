package com.example.demo.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String jwt;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String jwt, Long id, String username, String email, List<String> roles) {
        this.jwt = jwt;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
