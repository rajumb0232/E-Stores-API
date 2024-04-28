package com.self.flipcart.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.self.flipcart.exceptions.UserNotLoggedInException;
import com.self.flipcart.repository.AccessTokenRepo;
import com.self.flipcart.util.SimpleResponseStructure;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private CustomUserDetailsService userDetailsService;
    private AccessTokenRepo accessTokenRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                System.out.println("at: " + cookie.getValue());
                if (cookie.getName().equals("at")) accessToken = cookie.getValue();
            }

        try {
            log.info("Authenticating Token with JWT Filter...");
            String username = null;
            String role = null;

            if (accessToken != null) {
                log.info("Extracting username...");
                if(accessTokenRepo.existsByTokenAndIsBlocked(accessToken, true)) throw  new UserNotLoggedInException("Failed to authenticate the user");
                username = jwtService.extractUsername(accessToken);
                role = jwtService.extractUserRole(accessToken);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Creating authentication token...");
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
                        null, Collections.singleton(new SimpleGrantedAuthority(role)));
                token.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
                System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
                log.info("JWT Authentication Successful");
            }

        } catch (ExpiredJwtException ex) {
            handleException(ex, response, "Your AccessToken is expired, refresh your login");
        } catch (JwtException ex) {
            handleException(ex, response, "Authentication Failed");
        }
        filterChain.doFilter(request, response);
    }

    private void handleException(RuntimeException ex, HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("Application/json");
        response.setHeader("error", ex.getMessage());
        SimpleResponseStructure structure = new SimpleResponseStructure()
                .setStatus(HttpStatus.UNAUTHORIZED.value())
                .setMessage(message + " | " + ex.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), structure);
    }
}