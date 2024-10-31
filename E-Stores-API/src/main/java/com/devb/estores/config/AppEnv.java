package com.devb.estores.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppEnv {

    private final String baseURL;
    private final String jwtSecret;
    private final long jwtAccessExpirationSeconds;
    private final long jwtRefreshExpirationSeconds;

    public AppEnv(
            @Value("${app.base-url}") String baseURL,
            @Value("${app.jwt.secret}") String jwtSecret,
            @Value("${app.jwt.access-expiration-seconds}") long jwtAccessExpirationSeconds,
            @Value("${app.jwt.refresh-expiration-seconds}") long jwtRefreshExpirationSeconds) {
        this.baseURL = baseURL;
        this.jwtSecret = jwtSecret;
        this.jwtAccessExpirationSeconds = jwtAccessExpirationSeconds;
        this.jwtRefreshExpirationSeconds = jwtRefreshExpirationSeconds;
    }
}
