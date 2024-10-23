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
                .body(ResponseStructure.<T>builder()
                        .status(status.value())
                        .message(message)
                        .data(data)
                        .build());
    }

    public <T> ResponseEntity<ResponseStructure<T>> success(HttpStatus status, HttpHeaders headers, String message, T data) {
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(ResponseStructure.<T>builder()
                        .status(status.value())
                        .message(message)
                        .data(data)
                        .build());
    }

    public ResponseEntity<SimpleResponseStructure> success(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(SimpleResponseStructure.builder()
                        .message(message)
                        .status(status.value())
                        .build());
    }

    public ResponseEntity<SimpleResponseStructure> success(HttpStatus status, HttpHeaders headers, String message) {
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(SimpleResponseStructure.builder()
                        .message(message)
                        .status(status.value())
                        .build());
    }
}
