package com.example.demo.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.security.services.implementations.UserDetailsImpl;
import io.jsonwebtoken.*;

import static com.example.demo.constants.GeneralConstants.JWT_EXPIRATION;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * Generate jwt token
     * @param authentication the authentication
     * @return the jwt
     */
    public String generateJwtToken(Authentication authentication) {
        logger.info("Generating jwt token.");
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        logger.info("Generating jwt token success.");
        return token;
    }


    /**
     * Get username from jwt token
     * @param token the jwt token
     * @return the username
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validate a jwt token
     * @param authToken the jwt token
     * @return the validated boolean
     */
    public boolean validateJwtToken(String authToken) {
        try {
            logger.info("Validating jwt token.");
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            logger.info("Validating jwt token success.");
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
