package com.devb.estores.securityfilters;

import com.devb.estores.util.SimpleResponseStructure;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class FilterExceptionHandler {

    private FilterExceptionHandler() {
        /*
         * Created private constructor to avoid Instantiation of class
         * */
    }

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
