package com.self.flipcart.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailNotFoundException extends RuntimeException {
    String message;
}
