package com.devb.estores.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AppResponseBuilder {

    public <T> ResponseEntity<ResponseStructure<T>> success(HttpStatus status, String message, T data) {
        return ResponseEntity
                .status(status)
                .body(ResponseStructure.create(status.value(), message, data));
    }

    public <T> ResponseEntity<ResponseStructure<T>> success(HttpStatus status, HttpHeaders headers, String message, T data) {
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(ResponseStructure.create(status.value(), message, data));
    }

    public ResponseEntity<SimpleResponseStructure> success(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(SimpleResponseStructure.create(status.value(), message));
    }

    public ResponseEntity<SimpleResponseStructure> success(HttpStatus status, HttpHeaders headers, String message) {
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(SimpleResponseStructure.create(status.value(), message));
    }

    public <T> ResponseEntity<T> success(HttpStatus status, T data) {
        return ResponseEntity
                .status(status)
                .body(data);
    }
}
