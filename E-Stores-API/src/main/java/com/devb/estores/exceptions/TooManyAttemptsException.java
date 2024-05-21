package com.devb.estores.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TooManyAttemptsException extends RuntimeException {
    private String message;
}
