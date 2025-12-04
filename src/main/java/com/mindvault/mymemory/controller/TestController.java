package com.mindvault.mymemory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "Test secure API access.")
public class TestController {

    @Operation(
        summary = "Access a protected resource",
        description = "Requires a valid JWT token in the Authorization header."
    )
    @SecurityRequirement(name = "bearerAuth") // Links to the security scheme defined in OpenApiConfig
    @GetMapping("/secure")
    public String getSecureData(@AuthenticationPrincipal UserDetails userDetails) {
        return "Hello, " + userDetails.getUsername() + "! You have successfully accessed a secure endpoint.";
    }
}