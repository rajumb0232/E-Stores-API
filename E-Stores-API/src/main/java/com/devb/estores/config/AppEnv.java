package com.devb.estores.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppEnv {

    private String baseURL;
    private String domain;
    private boolean isHttps;
    private Jwt jwt;

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private long accessExpirationSeconds;
        private long refreshExpirationSeconds;
    }
}
