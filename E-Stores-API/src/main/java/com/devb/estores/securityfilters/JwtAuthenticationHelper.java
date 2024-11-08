package com.devb.estores.securityfilters;

import com.devb.estores.enums.TokenType;
import com.devb.estores.exceptions.InvalidJwtException;
import com.devb.estores.security.JwtService;
import com.devb.estores.service.TokenIdService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationHelper {

    private final JwtService jwtService;
    private final TokenIdService tokenIdService;

    public void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        if (userDetails.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    userDetails.getPassword(), userDetails.getAuthorities());
            token.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

    public UserDetails authenticateToken(String token, String deviceId, String browserName, String secChUaPlatform, String secChUaMobile, String userAgent) throws InvalidJwtException {
        log.info("Extracting credentials...");

        Claims claims = jwtService.extractClaims(token);

        if(claims == null)
            throw new InvalidJwtException("Failed to extract claims");

        String username = jwtService.getUsername(claims);
        List<String> roles = jwtService.getUserRoles(claims);

        log.info("Extracting client hints...");
        String browserNameInToken = jwtService.getBrowserName(claims);
        String secChUaPlatformInToken = jwtService.getSecChUaPlatform(claims);
        String secChUaMobileInToken = jwtService.getSecChUaMobile(claims);
        String userAgentInToken = jwtService.getUserAgent(claims);

        log.info("Extracting JTI with key: {}", username + "." + deviceId);
        String jti = jwtService.getJwtId(claims);
        String cachedJti = tokenIdService.getJti(username, deviceId, TokenType.REFRESH);

        log.info("Validating client hints and JTI");
        if (!Objects.equals(jti, cachedJti) ||
                !Objects.equals(browserNameInToken, browserName) ||
                !Objects.equals(secChUaMobileInToken, secChUaMobile) ||
                !Objects.equals(secChUaPlatformInToken, secChUaPlatform) ||
                !Objects.equals(userAgentInToken, userAgent))
            throw new InvalidJwtException("Failed to authenticate the refresh token, could not identify the token");

        log.info("user is valid");
        return this.getUserDetails(roles, username);
    }

    private UserDetails getUserDetails(List<String> roles, String username) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return roles.stream().map(SimpleGrantedAuthority::new).toList();
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return username;
            }
        };
    }
}
