package com.self.flipcart.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAlreadyExistsByEmailException extends RuntimeException {
private String message;
}
