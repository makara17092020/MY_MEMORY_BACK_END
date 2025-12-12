package com.mindvault.mymemory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Maps custom properties prefixed with 'jwt' from application.properties
 * to Java fields, eliminating the 'unknown property' IDE warning.
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    // Must match the property name 'jwt.secret'
    private String secret;

    // Maps 'jwt.expiration-ms' from application.properties
    private long expirationMs = 86400000; // default 24h

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}