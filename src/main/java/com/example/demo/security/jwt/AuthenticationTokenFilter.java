package com.example.demo.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.models.User;
import com.example.demo.security.services.blueprints.ICacheService;
import com.example.demo.security.services.implementations.JwtUserDetailsService;
import com.example.demo.security.services.implementations.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private ICacheService cacheService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

    /**
     * Filter incoming requests
     * @param request the http servlet request
     * @param response the http servlet response
     * @param filterChain the filter chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("Filtering incoming request.");
        String jwt = parseJwt(request);
        UserDetails userDetails;
        if (jwt != null) {
            User user = cacheService.getLoggedInUser(jwt);
            if (user != null) {
                logger.info("Implementing user details.");
                userDetails =  UserDetailsImpl.build(user);
            } else if (jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                userDetails = userDetailsService.loadUserByUsername(username);
            } else {
                logger.error("Cannot authenticate user");
                filterChain.doFilter(request, response);
                return;
            }

            logger.info("Setting security context.");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Setting security context success.");
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        logger.info("Parsing jwt.");
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            logger.info("Jwt parsed successfully.");
            return headerAuth.substring(7);
        }
        logger.error("Cannot parse jwt.");
        return null;
    }
}