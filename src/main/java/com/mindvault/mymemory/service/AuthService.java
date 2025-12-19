package com.mindvault.mymemory.service;

import com.mindvault.mymemory.dto.AuthRequest;
import com.mindvault.mymemory.dto.AuthResponse;
import com.mindvault.mymemory.dto.RegisterRequest;
import com.mindvault.mymemory.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userService.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setPassword(request.password());

        User savedUser = userService.register(newUser);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", savedUser.getId());
        extraClaims.put("username", savedUser.getUsername());
        extraClaims.put("name", savedUser.getName());
        extraClaims.put("email", savedUser.getEmail());

        String jwt = jwtService.generateToken(extraClaims, savedUser);

        return new AuthResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getName(), savedUser.getEmail(), jwt);
    }

    public AuthResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (AuthenticationException ex) {
            throw ex; // will be caught by GlobalExceptionHandler → 401
        }

        User user = (User) userService.loadUserByUsername(request.username());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());
        extraClaims.put("username", user.getUsername());
        extraClaims.put("name", user.getName());
        extraClaims.put("email", user.getEmail());

        String jwt = jwtService.generateToken(extraClaims, user);

        return new AuthResponse(user.getId(), user.getUsername(), user.getName(), user.getEmail(), jwt);
    }
}
