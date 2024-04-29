package com.self.flipcart.securityfilters;

import com.self.flipcart.exceptions.UserNotLoggedInException;
import com.self.flipcart.repository.RefreshTokenRepo;
import com.self.flipcart.security.JwtService;
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
import java.util.Collections;

@AllArgsConstructor
@Slf4j
public class RefreshFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private RefreshTokenRepo refreshTokenRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authenticating Token with Refresh Filter...");
        String rt = null;
        if(request.getCookies() != null){
           for (Cookie cookie : request.getCookies()){
               if(cookie.getName().equals("rt")) rt = cookie.getValue();
           }
        }

        if(rt == null) throw new UserNotLoggedInException("User not logged in | no credentials found");
        if(refreshTokenRepo.existsByTokenAndIsBlocked(rt, true)) throw new UserNotLoggedInException("Access blocked | try login again");

        try{
            log.info("Extracting credentials...");
            String username = jwtService.extractUsername(rt);
            String role = jwtService.extractUserRole(rt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
                        null, Collections.singleton(new SimpleGrantedAuthority(role)));
                token.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
                log.info("JWT Authentication Successful");
            }
        }catch (ExpiredJwtException ex) {
            FilterExceptionHandler.handleException(response, "Your refreshToken is expired, try login again");
        } catch (JwtException ex) {
            FilterExceptionHandler.handleException(response, "Authentication Failed | " + ex.getMessage());
        } catch (UserNotLoggedInException ex){
            log.info("Authentication failed | User already logged in");
            FilterExceptionHandler.handleException(response, ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
