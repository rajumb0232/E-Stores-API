package com.self.flipcart.securityfilters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.self.flipcart.util.SimpleResponseStructure;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class FilterExceptionHandler {

    public static void handleException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("Application/json");
        response.setHeader("error", message);
        SimpleResponseStructure structure = new SimpleResponseStructure()
                .setStatus(HttpStatus.UNAUTHORIZED.value())
                .setMessage(message);
        new ObjectMapper().writeValue(response.getOutputStream(), structure);
    }
}
