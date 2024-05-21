package com.devb.estores.exceptionhandlers;

import com.devb.estores.exceptions.InvalidProductTypeNameException;
import com.devb.estores.exceptions.InvalidSubCategoryException;
import com.devb.estores.exceptions.InvalidTopCategoryException;
import com.devb.estores.exceptions.ProductTypeNotFoundException;
import com.devb.estores.util.ErrorStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class ProductRelatedExceptionHandler {

    private ErrorResponse errorResponse;

    @ExceptionHandler
    public ResponseEntity<ErrorStructure<String>> handleProductTypeNotFound(ProductTypeNotFoundException ex){
        return errorResponse.structure(HttpStatus.NOT_FOUND, ex.getMessage(), "Product Type not found with the given Name");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure<String>> handleInvalidTopCategory(InvalidTopCategoryException ex){
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "No Top Categories available by the given name");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure<String>> handleInvalidSubCategory(InvalidSubCategoryException ex){
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "No Sub Categories available by the given name");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure<String>> handleInvalidProductTypeName(InvalidProductTypeNameException ex){
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid Product Type name, should only contain alphabetical characters");
    }

}
