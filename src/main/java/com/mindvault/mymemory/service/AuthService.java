package com.mindvault.mymemory.service;

import com.mindvault.mymemory.dto.AuthRequest;
import com.mindvault.mymemory.dto.RegisterRequest;
import com.mindvault.mymemory.dto.AuthResponse;
import com.mindvault.mymemory.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
        // 1. Authenticate the user credentials
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. Load UserDetails to generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        // Try to obtain the domain User to include id, name and email
        Long userId = null;
        String name = null;
        String email = null;
        if (userDetails instanceof User) {
            User domainUser = (User) userDetails;
            userId = domainUser.getId();
            name = domainUser.getName();
            email = domainUser.getEmail();
        }

        // 3. Prepare extra claims and generate token
        Map<String, Object> extraClaims = new HashMap<>();
        if (userId != null) extraClaims.put("id", userId);
        extraClaims.put("username", userDetails.getUsername());
        if (name != null) extraClaims.put("name", name);
        if (email != null) extraClaims.put("email", email);

        final String jwt = jwtService.generateToken(extraClaims, userDetails);

        return new AuthResponse(userId, userDetails.getUsername(), name, email, jwt);
    }
}