package com.self.flipcart.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationSessionExpiredException extends RuntimeException {
    private String message;
}
