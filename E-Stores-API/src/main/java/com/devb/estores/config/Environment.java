package com.devb.estores.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Environment {

    private final String baseURL;
    private final String jwtSecret;
    private final long jwtAccessExpirationSeconds;
    private final long jwtRefreshExpirationSeconds;

    public Environment(
            @Value("${app.base_url}") String baseURL,
            @Value("${app.jwt.secret}") String jwtSecret,
            @Value("${app.jwt.access_expiration_seconds}") long jwtAccessExpirationSeconds,
            @Value("${app.jwt.refresh_expiration_seconds}") long jwtRefreshExpirationSeconds) {
        this.baseURL = baseURL;
        this.jwtSecret = jwtSecret;
        this.jwtAccessExpirationSeconds = jwtAccessExpirationSeconds;
        this.jwtRefreshExpirationSeconds = jwtRefreshExpirationSeconds;
    }
}
