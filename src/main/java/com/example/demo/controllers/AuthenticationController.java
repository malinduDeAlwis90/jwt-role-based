package com.example.demo.controllers;

import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.payload.response.RegistrationRequest;
import com.example.demo.security.services.blueprints.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    IAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        JwtResponse jwtResponse = authenticationService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        MessageResponse response = authenticationService.registerUser(request.getUsername(), request.getPassword(), request.getEmail(), request.getRoles());

        if (response == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username already exists"));
        }
        return ResponseEntity.ok(response);
    }
}
