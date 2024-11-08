package com.devb.estores.securityfilters;

import com.devb.estores.exceptions.InvalidJwtException;
import com.devb.estores.exceptions.UserNotLoggedInException;
import com.devb.estores.security.RequestUtils;
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

    private final JwtAuthenticationHelper authenticationHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authenticating RefreshToken with Refresh Filter...");
        String rt = RequestUtils.extractCookie("rt", request.getCookies());

        String deviceId = RequestUtils.extractDeviceId(request.getCookies());
        log.info("Is device Id found? => {}", deviceId != null);

        if (rt == null) throw new UserNotLoggedInException("User not logged in | no credentials found");
        try {
            String browserName = RequestUtils.extractBrowserName(request.getHeader(RequestUtils.SEC_CH_UA));
            UserDetails userDetails = authenticationHelper.authenticateToken(rt,
                    deviceId,
                    browserName,
                    request.getHeader(RequestUtils.SEC_CH_UA_PLATFORM),
                    request.getHeader(RequestUtils.SEC_CH_UA_MOBILE),
                    request.getHeader(RequestUtils.USER_AGENT));

            /* Setting authentication
             * */
            authenticationHelper.setAuthentication(userDetails, request);
            log.info("JWT Authentication Successful");

        filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            RequestUtils.handleException(response, "Your refreshToken is expired, try login again");
        } catch (JwtException ex) {
            RequestUtils.handleException(response, "Authentication Failed | " + ex.getMessage());
        } catch (UserNotLoggedInException ex) {
            log.info("Authentication failed | User already logged in");
            RequestUtils.handleException(response, ex.getMessage());
        } catch (InvalidJwtException ex) {
            log.info(ex.getMessage());
            RequestUtils.handleException(response, ex.getMessage());
        }

    }
}
