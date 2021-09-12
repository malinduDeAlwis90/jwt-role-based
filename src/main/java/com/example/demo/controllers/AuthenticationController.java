package com.example.demo.controllers;

import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.payload.response.RegistrationRequest;
import com.example.demo.security.services.blueprints.IAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    IAuthenticationService authenticationService;

    /**
     * Login a user
     * @param loginRequest the login request
     * @return the jwt response
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("Incoming password login request.");
        JwtResponse jwtResponse = authenticationService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        log.info("Successfully authenticated.");
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Register a user
     * @param request the registration request
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        log.info("Incoming registration request.");
        MessageResponse response = authenticationService.registerUser(request.getUsername(), request.getPassword(), request.getEmail(), request.getRoles());

        if (response == null) {
            log.error(" Username already exists");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username already exists"));
        }
        log.info("User registration success.");
        return ResponseEntity.ok(response);
    }
}
