package com.mindvault.mymemory.service;

import com.mindvault.mymemory.dto.AuthRequest;
import com.mindvault.mymemory.dto.AuthResponse;
import com.mindvault.mymemory.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Registers a new user and generates a JWT token for them.
     */
    public AuthResponse register(AuthRequest request) {
        // Map DTO to Entity
        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(request.password());

        User savedUser = userService.register(newUser);

        // Generate token immediately after registration
        final String jwt = jwtService.generateToken(savedUser);
        
        return new AuthResponse(savedUser.getUsername(), jwt);
    }

    /**
     * Authenticates an existing user and generates a JWT token.
     */
    public AuthResponse login(AuthRequest request) {
        // 1. Authenticate the user credentials
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. Load UserDetails to generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        
        // 3. Generate token
        final String jwt = jwtService.generateToken(userDetails);

        return new AuthResponse(userDetails.getUsername(), jwt);
    }
}