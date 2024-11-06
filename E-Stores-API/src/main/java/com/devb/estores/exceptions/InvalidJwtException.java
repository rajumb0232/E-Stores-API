package com.devb.estores.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidJwtException extends RuntimeException {
    private final String message;
}
