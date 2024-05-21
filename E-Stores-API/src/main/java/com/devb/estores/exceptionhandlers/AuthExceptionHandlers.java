package com.devb.estores.exceptionhandlers;

import com.devb.estores.exceptions.*;
import com.devb.estores.util.ErrorStructure;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class AuthExceptionHandlers {

    private ErrorResponse errorResponse;

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorStructure<String>> handleEmailNotFound(EmailNotFoundException ex) {
        log.error(ex.getMessage()+" | "+"invalid email id");
        return errorResponse.structure(HttpStatus.NOT_FOUND, ex.getMessage(), "invalid email id");
    }

    @ExceptionHandler(RegistrationSessionExpiredException.class)
    public ResponseEntity<ErrorStructure<String>> handleRegistrationSessionExpired(RegistrationSessionExpiredException ex){
        log.error(ex.getMessage()+" | "+"Took too long to register, the Registration session Expired");
        return errorResponse.structure(HttpStatus.EXPECTATION_FAILED, ex.getMessage(), "Took too long to register, the Registration session Expired. Try again");
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorStructure<String>> handleDuplicateEmail(DuplicateEmailException ex) {
        log.error(ex.getMessage()+" | "+"User already exists with the given email id");
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "User already exists with the given email id");
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ErrorStructure<String>> handleOtpExpired(OtpExpiredException ex){
        log.error(ex.getMessage()+" | "+"The given OTP is expired");
        return errorResponse.structure(HttpStatus.EXPECTATION_FAILED, ex.getMessage(), "The given OTP is expired");
    }

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<ErrorStructure<String>> handleUserNotFound(UserNotFoundByIdException ex){
        log.error(ex.getMessage()+" | "+ "User not found with the given Id");
        return errorResponse.structure(HttpStatus.NOT_FOUND, ex.getMessage(), "User not found with the given Id");
    }

    @ExceptionHandler(IncorrectOTPException.class)
    public ResponseEntity<ErrorStructure<String>> handleIncorrectOTP(IncorrectOTPException ex){
        log.error(ex.getMessage()+" | "+"The given OTP is Incorrect");
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "The given OTP is Incorrect");
    }

    @ExceptionHandler(UserAlreadyExistsByEmailException.class)
    public ResponseEntity<ErrorStructure<String>> handleUserAlreadyExistsByEmail(UserAlreadyExistsByEmailException ex){
        log.error(ex.getMessage()+" | "+"User Already exists with the given email");
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "User Already exists with the given email");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorStructure<String>> handleAccessDenied(AccessDeniedException ex){
        log.error(ex.getMessage()+" | "+"you are not allowed to access this resource");
        return errorResponse.structure(HttpStatus.FORBIDDEN, ex.getMessage(), "you are not allowed to access this resource");
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<ErrorStructure<String>> handleUserNotLoggedIn(UserNotLoggedInException ex){
        log.error(ex.getMessage()+" | "+"Login Expired or not logged in, please login again");
        return errorResponse.structure(HttpStatus.UNAUTHORIZED, ex.getMessage(), "Login Expired or not logged in, please login again");
    }

    @ExceptionHandler(UserAlreadyLoggedInException.class)
    public ResponseEntity<ErrorStructure<String>> handleUserAlreadyLoggedIn(UserAlreadyLoggedInException ex){
        log.error(ex.getMessage()+" | "+"You are already logged in, logout or clear cookie to login again");
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "You are already logged in, logout or clear cookie to login again");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorStructure<String>> handleUsernameNotFound(UsernameNotFoundException ex){
        log.error(ex.getMessage()+" | "+"Failed to find user");
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Failed to find user");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorStructure<String>> handleBadCredentials(BadCredentialsException ex){
        log.error(ex.getMessage()+" | "+"The given username or password is incorrect");
        return errorResponse.structure(HttpStatus.FORBIDDEN, ex.getMessage(), "The given username or password is incorrect");
    }

    @ExceptionHandler(TooManyAttemptsException.class)
    public ResponseEntity<ErrorStructure<String>> handleTooManyAttempts(TooManyAttemptsException ex){
        log.error(ex.getMessage()+" | "+"Too Many Requests at once");
        return errorResponse.structure(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(), "Too many requests at once, try after 10 seconds");
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<ErrorStructure<String>> handleInvalidUserRole(InvalidUserRoleException ex){
        log.error(ex.getMessage()+" | "+"Invalid User role specified");
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid User role specified");
    }
}
