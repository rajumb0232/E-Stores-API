package com.devb.estores.securityfilters;

import com.devb.estores.enums.TokenType;
import com.devb.estores.exceptions.InvalidJwtException;
import com.devb.estores.security.JwtService;
import com.devb.estores.service.TokenIdService;
import com.devb.estores.util.SimpleResponseStructure;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class FilterHelper {

    private final JwtService jwtService;
    private final TokenIdService tokenIdService;

    public static final String SEC_CH_UA = "sec-ch-ua";
    public static final String SEC_CH_UA_PLATFORM = "sec-ch-ua-platform";
    public static final String SEC_CH_UA_MOBILE = "sec-ch-ua-mobile";
    public static final String USER_AGENT = "user-agent";

    public static String extractCookie(String cookieName, Cookie[] cookies) {
        String cookieValue = null;
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        return cookieValue;
    }

    public static void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        if (userDetails.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    userDetails.getPassword(), userDetails.getAuthorities());
            token.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

    public static void handleException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("Application/json");
        response.setHeader("error", message);
        SimpleResponseStructure structure = SimpleResponseStructure.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(message)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), structure);
    }

    public static String extractDeviceId(Cookie[] cookies) {
        String did = "";
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("did")) {
                    did = cookie.getValue();
                    break;
                }
            }
        return did;
    }

    /**
     * Helps in extracting the browser name from the given secChUa
     */
    public static String extractBrowserName(String secChUa) {
        if (secChUa != null) {
            int start = secChUa.indexOf('"') + 1;
            int end = secChUa.indexOf("\"", start);
            return secChUa.substring(start, end);
        } else
            return null;
    }

    public UserDetails authenticateToken(String token, String deviceId, String browserName, String secChUaPlatform, String secChUaMobile, String userAgent) {
        log.info("Extracting credentials...");

        Claims claims = jwtService.extractClaims(token);
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
        if (!jti.equals(cachedJti) ||
                !browserNameInToken.equals(browserName) ||
                !secChUaMobileInToken.equals(secChUaMobile) ||
                !secChUaPlatformInToken.equals(secChUaPlatform) ||
                !userAgentInToken.equals(userAgent))
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