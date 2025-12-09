package com.mindvault.mymemory.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * AuthRequest: Record for handling user login/registration requests.
 * Uses Java Records, providing automatic accessor methods (e.g., request.username()) and constructors.
 */
public record AuthRequest(
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "Email is required") String email,
    @NotBlank(message = "Username is required") String username,
    @NotBlank(message = "Password is required") String password) {

    public String name() {
        return name;
    }}