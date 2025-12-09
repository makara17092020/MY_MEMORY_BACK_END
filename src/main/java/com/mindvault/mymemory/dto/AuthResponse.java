package com.mindvault.mymemory.dto;

/**
 * AuthResponse: Record for sending the JWT and user info back to the client.
 * Now includes the user id, username and token.
 */
public record AuthResponse(
    Long id,
    String username,
    String name,
    String email,
    String token) {}