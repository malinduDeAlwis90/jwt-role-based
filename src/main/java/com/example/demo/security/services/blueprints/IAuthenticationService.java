package com.example.demo.security.services.blueprints;

import com.example.demo.models.RoleEnum;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAuthenticationService {


    /**
     * Authenticate a user
     * @param username the username
     * @param password the password
     * @return the jwt response
     */
    JwtResponse authenticateUser(String username, String password);

    /**
     * Register a user
     * @param username tha username
     * @param password the password
     * @param email the email
     * @param inputRoles the list of roles
     * @return the success response with message
     */
    MessageResponse registerUser(String username, String password, String email, List<RoleEnum> inputRoles);
}
