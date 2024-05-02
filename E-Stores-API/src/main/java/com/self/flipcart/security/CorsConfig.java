package com.self.flipcart.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
       registry.addMapping("/**")
               .allowedOriginPatterns("http://localhost:5173")
               .allowCredentials(true)
               .allowedHeaders("*")
               .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
