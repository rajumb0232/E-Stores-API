package com.devb.estores.securityfilters;

import com.devb.estores.exceptions.UserNotLoggedInException;
import com.devb.estores.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authenticating FingerPrint with JWT Filter...");
        String accessToken = FilterHelper.extractCookie("at", request.getCookies());


        try {
            if (accessToken == null) throw new UserNotLoggedInException("Failed to authenticate the user");
//            if (accessTokenRepo.existsByTokenAndIsBlocked(accessToken, true))
//                throw new UserNotLoggedInException("Failed to authenticate the user");

            log.info("Extracting credentials...");

//            String username = jwtService.getUsername(accessToken);
//            String roles = jwtService.getUserRoles(accessToken);
//
//            FilterHelper.setAuthentication(username, roles, request);
            log.info("Authentication Successful");

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            FilterHelper.handleException(response, "Your AccessToken is expired, refresh your login");
        } catch (JwtException ex) {
            FilterHelper.handleException(response, "Authentication Failed | " + ex.getMessage());
        } catch (UserNotLoggedInException ex) {
            log.info("Authentication failed | User not logged in");
            FilterHelper.handleException(response, "User not logged in | send a refresh request or try again after clearing cookies");
        }

    }
}