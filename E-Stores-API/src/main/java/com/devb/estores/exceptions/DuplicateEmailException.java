package com.devb.estores.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicateEmailException extends RuntimeException {
  private final String message;
}
