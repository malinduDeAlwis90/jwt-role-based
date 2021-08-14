package com.example.demo.payload.response;

import com.example.demo.models.RoleEnum;
import lombok.Data;

import java.util.List;

@Data
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    private List<RoleEnum> roles;
}
