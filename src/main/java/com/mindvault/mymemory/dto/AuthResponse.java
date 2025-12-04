package com.mindvault.mymemory.dto;

/**
 * AuthResponse: Record for sending the JWT and username back to the client.
 * Records automatically provide the required constructor (AuthResponse(String, String)).
 */
public record AuthResponse(
    String username,
    String token) {}