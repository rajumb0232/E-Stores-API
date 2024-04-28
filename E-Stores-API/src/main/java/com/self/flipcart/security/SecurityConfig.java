package com.self.flipcart.security;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private CustomUserDetailsService userDetailService;
    private JwtFilter jwtFilter;
    private TestFilter testFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Order(1)
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .securityMatchers(matcher -> matcher.requestMatchers("/api/fkv1"))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/fkv1/**").permitAll())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain2(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .securityMatchers(matcher -> matcher.requestMatchers("/d"))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/d/**").permitAll())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    FilterRegistrationBean<JwtFilter> jwtFilterRegistration(){
        FilterRegistrationBean<JwtFilter> registry = new FilterRegistrationBean<>();
        registry.setFilter(jwtFilter);
        registry.addUrlPatterns("/api/fkv1/*");
        registry.setOrder(2);
        return registry;
    }

    @Bean
    FilterRegistrationBean<TestFilter> testFilterRegistration(){
        FilterRegistrationBean<TestFilter> registry = new FilterRegistrationBean<>();
        registry.setFilter(testFilter);
        registry.addUrlPatterns("/d/*");
        registry.setOrder(1);
        return registry;
    }

//    private AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder());
//        provider.setUserDetailsService(userDetailService);
//        return provider;
//    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
