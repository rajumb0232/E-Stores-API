package com.devb.estores.securityfilters;

import com.devb.estores.exceptions.InvalidJwtException;
import com.devb.estores.exceptions.UserNotLoggedInException;
import com.devb.estores.security.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtAuthenticationHelper authenticationHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authenticating AccessToken with JWT Filter...");
        String accessToken = RequestUtils.extractCookie("at", request.getCookies());

        String deviceId = RequestUtils.extractDeviceId(request.getCookies());
        log.info("Is device Id found? => {}", deviceId != null);

        if (accessToken == null) throw new UserNotLoggedInException("Failed to authenticate the user");
        try {
            String browserName = RequestUtils.extractBrowserName(request.getHeader(RequestUtils.SEC_CH_UA));
            UserDetails userDetails = authenticationHelper.authenticateToken(accessToken,
                    deviceId,
                    browserName,
                    request.getHeader(RequestUtils.SEC_CH_UA_PLATFORM),
                    request.getHeader(RequestUtils.SEC_CH_UA_MOBILE),
                    request.getHeader(RequestUtils.USER_AGENT));

            /* Setting authentication
             * */
            authenticationHelper.setAuthentication(userDetails, request);
            log.info("Authentication Successful");

            filterChain.doFilter(request, response);
        } catch (UserNotLoggedInException ex) {
            log.info("Authentication failed | User not logged in");
            RequestUtils.handleException(response, "User not logged in | send a refresh request or try again after clearing cookies");
        } catch (InvalidJwtException ex) {
            log.info("{} | invalid JWT used", ex.getMessage());
            RequestUtils.handleException(response, ex.getMessage());
        }

    }
}