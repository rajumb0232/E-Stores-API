package com.devb.estores.securityfilters;

import com.devb.estores.exceptions.InvalidJwtException;
import com.devb.estores.exceptions.UserNotLoggedInException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class RefreshFilter extends OncePerRequestFilter {

    private final FilterHelper filterHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authenticating RefreshToken with Refresh Filter...");
        String rt = FilterHelper.extractCookie("rt", request.getCookies());

        String deviceId = FilterHelper.extractDeviceId(request.getCookies());
        log.info("Is device Id found? => {}", deviceId != null);

        if (rt == null) throw new UserNotLoggedInException("User not logged in | no credentials found");
        try {
            String browserName = FilterHelper.extractBrowserName(request.getHeader(FilterHelper.SEC_CH_UA));
            UserDetails userDetails = filterHelper.authenticateToken(rt,
                    deviceId,
                    browserName,
                    request.getHeader(FilterHelper.SEC_CH_UA_PLATFORM),
                    request.getHeader(FilterHelper.SEC_CH_UA_MOBILE),
                    request.getHeader(FilterHelper.USER_AGENT));

            /* Setting authentication
             * */
            FilterHelper.setAuthentication(userDetails, request);
            log.info("JWT Authentication Successful");

        filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            FilterHelper.handleException(response, "Your refreshToken is expired, try login again");
        } catch (JwtException ex) {
            FilterHelper.handleException(response, "Authentication Failed | " + ex.getMessage());
        } catch (UserNotLoggedInException ex) {
            log.info("Authentication failed | User already logged in");
            FilterHelper.handleException(response, ex.getMessage());
        } catch (InvalidJwtException ex) {
            log.info(ex.getMessage());
            FilterHelper.handleException(response, ex.getMessage());
        }

    }
}
