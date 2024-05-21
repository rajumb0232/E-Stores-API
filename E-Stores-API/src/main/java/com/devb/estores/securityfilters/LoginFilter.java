package com.devb.estores.securityfilters;

import com.devb.estores.exceptions.UserAlreadyLoggedInException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class LoginFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Verifying login request...");
        try {
            if(request.getCookies() != null){
                Arrays.asList(request.getCookies()).forEach(cookie -> {
                    if(cookie.getName().equals("at") || cookie.getName().equals("rt")) {
                    throw new UserAlreadyLoggedInException("User already logged in | try sending refresh request or clear cookies");
                    }
                });
            }
            log.info("Login request is authentic");
        }catch (UserAlreadyLoggedInException e){
            log.info("Failed to login, user already logged in");
            FilterExceptionHandler.handleException(response, e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
