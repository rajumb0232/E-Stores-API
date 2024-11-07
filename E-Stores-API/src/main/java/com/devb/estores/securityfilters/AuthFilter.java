package com.devb.estores.securityfilters;

import com.devb.estores.cache.CacheName;
import com.devb.estores.cache.CacheService;
import com.devb.estores.exceptions.InvalidJwtException;
import com.devb.estores.exceptions.UserNotLoggedInException;
import com.devb.estores.security.JwtService;
import io.jsonwebtoken.Claims;
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
import java.util.List;

@Slf4j
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CacheService cacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authenticating AccessToken with JWT Filter...");
        String accessToken = FilterHelper.extractCookie("at", request.getCookies());

        String deviceId = FilterHelper.extractDeviceId(request.getCookies());
        log.info("Is device Id found? => {}", deviceId != null);

        if (accessToken == null) throw new UserNotLoggedInException("Failed to authenticate the user");
        try {
            log.info("Extracting credentials...");

            Claims claims = jwtService.extractClaims(accessToken);
            String username = jwtService.getUsername(claims);
            List<String> roles = jwtService.getUserRoles(claims);

            /* Validating jti
             * */
            log.info("Extracting JTI with key: {}", username + "." + deviceId);
            String jti = jwtService.getJwtId(claims);
            String cachedJti = cacheService.getEntry(CacheName.ACCESS_TOKEN_CACHE, username + "." + deviceId, String.class);

            log.info("Validating JTI in token: {}, and cache: {}", jti, cachedJti);
            if (!jti.equals(cachedJti))
                throw new InvalidJwtException("Failed to authenticate the access token");

            /* Setting authentication
             * */
            FilterHelper.setAuthentication(username, roles, request);
            log.info("Authentication Successful");

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            FilterHelper.handleException(response, "Your AccessToken is expired, refresh your login");
        } catch (JwtException ex) {
            FilterHelper.handleException(response, "Authentication Failed | " + ex.getMessage());
        } catch (UserNotLoggedInException ex) {
            log.info("Authentication failed | User not logged in");
            FilterHelper.handleException(response, "User not logged in | send a refresh request or try again after clearing cookies");
        }catch (InvalidJwtException ex) {
            log.info("{} | invalid JWT used", ex.getMessage());
            FilterHelper.handleException(response, ex.getMessage());
        }

    }
}