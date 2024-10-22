package com.devb.estores.exceptionhandlers;

import com.devb.estores.exceptions.InvalidDisplayTypeException;
import com.devb.estores.exceptions.InvalidPrimeCategoryException;
import com.devb.estores.exceptions.StoreNotFoundException;
import com.devb.estores.util.ErrorStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class StoreExceptionHandlers {

    private ErrorResponse errorResponse;

    @ExceptionHandler(InvalidDisplayTypeException.class)
    public ResponseEntity<ErrorStructure<String>> handleInvalidDisplayType(InvalidDisplayTypeException ex){
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid display type specified, should be any of basic, card or complete");
    }

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ErrorStructure<String>> handleStoreNotFoundById(StoreNotFoundException ex){
        return errorResponse.structure(HttpStatus.NOT_FOUND, ex.getMessage(), "No store found");
    }

    @ExceptionHandler(InvalidPrimeCategoryException.class)
    public ResponseEntity<ErrorStructure<String>> handleInvalidPrimeCategory(InvalidPrimeCategoryException ex){
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid prime category specified");
    }
}
