package com.self.flipcart.exceptionhandlers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
public class FieldErrorExceptionHandlers extends ResponseEntityExceptionHandler {

    private ErrorResponse errorResponse;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ObjectError> objectErrors = ex.getAllErrors();
        Map<String, String> errors = new HashMap<>();
        objectErrors.forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return errorResponse.structure(HttpStatus.BAD_REQUEST, "Invalid attributes", errors);
    }
}
