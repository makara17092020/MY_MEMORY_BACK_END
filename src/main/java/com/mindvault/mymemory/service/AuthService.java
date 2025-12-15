package com.mindvault.mymemory.service;

import com.mindvault.mymemory.dto.AuthRequest;
import com.mindvault.mymemory.dto.RegisterRequest;
import com.mindvault.mymemory.dto.AuthResponse;
import com.mindvault.mymemory.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user and generates a JWT token for them. The token will include basic user data as claims.
     */
    public AuthResponse register(RegisterRequest request) {
        // Map DTO to Entity
        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setUsername(request.username());
        newUser.setPassword(request.password());

        User savedUser = userService.register(newUser);

        // Prepare extra claims with user data
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", savedUser.getId());
        extraClaims.put("username", savedUser.getUsername());
        extraClaims.put("name", savedUser.getName());
        extraClaims.put("email", savedUser.getEmail());

        // Generate token with extra claims
        final String jwt = jwtService.generateToken(extraClaims, savedUser);

        return new AuthResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getName(), savedUser.getEmail(), jwt);
    }

    /**
     * Authenticates an existing user and generates a JWT token including basic user data as claims.
     */
    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for username: {}", request.username());

        // 1. Authenticate the user credentials
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        log.info("Authentication successful for username: {}", request.username());

        // 2. Load domain user (implements UserDetails) to generate the token and include user fields
        final User domainUser = (User) userService.loadUserByUsername(request.username());

        Long userId = domainUser.getId();
        String name = domainUser.getName();
        String email = domainUser.getEmail();

        // 3. Prepare extra claims and generate token
        Map<String, Object> extraClaims = new HashMap<>();
        if (userId != null) extraClaims.put("id", userId);
        extraClaims.put("username", domainUser.getUsername());
        if (name != null) extraClaims.put("name", name);
        if (email != null) extraClaims.put("email", email);

        final String jwt = jwtService.generateToken(extraClaims, domainUser);

        return new AuthResponse(userId, domainUser.getUsername(), name, email, jwt);
    }
}