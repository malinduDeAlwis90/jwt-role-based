package com.example.demo.security.services.blueprints;

import com.example.demo.models.RoleEnum;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAuthenticationService {

    JwtResponse authenticateUser(String username, String password);

    MessageResponse registerUser(String username, String password, String email, List<RoleEnum> inputRoles);
}
