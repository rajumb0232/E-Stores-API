package com.devb.estores.exceptionhandlers;

import com.devb.estores.util.ErrorStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ErrorResponse {

    public ResponseEntity<Object> structure(HttpStatus status, String message, Object rootCause) {
        return ResponseEntity.status(status).body((new ErrorStructure<>().setStatus(status.value())
                .setMessage(message).setRootCause(rootCause)));
    }

    public ResponseEntity<ErrorStructure<String>> structure(HttpStatus status, String message, String rootCause) {
        return ResponseEntity.status(status).body((new ErrorStructure<String>().setStatus(status.value())
                .setMessage(message).setRootCause(rootCause)));
    }
}
