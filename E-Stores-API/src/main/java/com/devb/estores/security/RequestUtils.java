package com.devb.estores.security;

import com.devb.estores.util.SimpleResponseStructure;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class RequestUtils {

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
}