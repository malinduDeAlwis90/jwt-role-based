package com.example.demo.security.services.implementations;

import com.example.demo.models.Role;
import com.example.demo.models.RoleEnum;
import com.example.demo.models.User;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.blueprints.IAuthenticationService;
import com.example.demo.security.services.blueprints.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ICacheService cacheService;

    public JwtResponse authenticateUser(String username, String password) {
        log.info("Authentication request start processing. Authenticate with username and password.");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        log.info("Populating user details.");
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        cacheService.saveLoggedInUser(jwtToken, userRepository.findByUsername(username).get());

        log.info("Authentication success. Generating response.");
        return new JwtResponse(jwtToken,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public MessageResponse registerUser(String username, String password, String email, List<RoleEnum> inputRoles) {
        log.info("Registration request start processing. Checking db user existence.");
        if (userRepository.existsByUsername(username)) {
            log.info("User already available in db.");
            return null;
        }

        // Create a new user
        log.info("Creating new user.");
        User user = new User(
                username,
                email,
                encoder.encode(password)
        );
        Set<Role> roles = new HashSet<>();

        if (inputRoles == null) {
            Role role = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Roles not found in request / db."));
            roles.add(role);
        } else {
            inputRoles.forEach(inputRole -> {
                Role role = roleRepository.findByName(inputRole)
                        .orElseThrow(() -> new RuntimeException("Error: Role not found in db."));
                roles.add(role);
            });
        }
        log.info("Setting roles.");
        user.setRoles(roles);
        log.info("Saving new user to db.");
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }
}
