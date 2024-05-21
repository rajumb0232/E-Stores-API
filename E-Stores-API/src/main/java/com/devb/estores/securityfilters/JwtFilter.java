package com.devb.estores.securityfilters;

import com.devb.estores.exceptions.UserNotLoggedInException;
import com.devb.estores.repository.AccessTokenRepo;
import com.devb.estores.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private AccessTokenRepo accessTokenRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authenticating Token with JWT Filter...");
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("at")) accessToken = cookie.getValue();
            }

        try {
            String username = null;
            String roles = null;

            if (accessToken != null) {
                log.info("Extracting credentials...");
                if (accessTokenRepo.existsByTokenAndIsBlocked(accessToken, true))
                    throw new UserNotLoggedInException("Failed to authenticate the user");
                username = jwtService.extractUsername(accessToken);
                roles = jwtService.extractUserRoles(accessToken);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                roles = roles.replace('[', ' ').replace(']', ' ').trim();
                List<String> roleList = Arrays.asList(roles.split(", "));
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
                        null, roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                token.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
                log.info("JWT Authentication Successful");
            }

        } catch (ExpiredJwtException ex) {
            FilterExceptionHandler.handleException(response, "Your AccessToken is expired, refresh your login");
        } catch (JwtException ex) {
            FilterExceptionHandler.handleException(response, "Authentication Failed | " + ex.getMessage());
        } catch (UserNotLoggedInException ex) {
            log.info("Authentication failed | User already logged in");
            FilterExceptionHandler.handleException(response, "User already logged in | send a refresh request or try again after clearing cookies");
        }
        filterChain.doFilter(request, response);
    }
}