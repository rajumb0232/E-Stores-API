package com.self.flipcart.exceptionhandlers;

import com.self.flipcart.util.ErrorStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ErrorResponse {
    private ErrorStructure<Object> structure;
    private ErrorStructure<String> stringStructure;

    public ResponseEntity<Object> structure(HttpStatus status, String message, Object rootCause) {
        return ResponseEntity.status(status).body((structure.setStatus(status.value())
                .setMessage(message).setRootCause(rootCause)));
    }

    public ResponseEntity<ErrorStructure<String>> structure(HttpStatus status, String message, String rootCause) {
        return ResponseEntity.status(status).body((stringStructure.setStatus(status.value())
                .setMessage(message).setRootCause(rootCause)));
    }
}
