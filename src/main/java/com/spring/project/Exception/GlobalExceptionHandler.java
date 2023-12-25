package com.spring.project.Exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponseContainer> handleClientNotFoundException(ClientNotFoundException ex, WebRequest webRequest) {
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponseContainer>(errorResponseContainer, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseContainer> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest webRequest){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponseContainer>(errorResponseContainer, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotAvailableException.class)
    public ResponseEntity<ErrorResponseContainer> handleEmailNotAvailableException(EmailNotAvailableException ex, WebRequest webRequest){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.GONE.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponseContainer>(errorResponseContainer, HttpStatus.GONE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseContainer> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()  // Get the first error message
                .orElse("Unknown validation error");

        errorResponseContainer.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponseContainer.setErrorMessage(errors);
        return new ResponseEntity<ErrorResponseContainer>(errorResponseContainer, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseContainer> handleConstraintViolationException(ConstraintViolationException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponseContainer>(errorResponseContainer, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponseContainer> handleExpiredJwtException(ExpiredJwtException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponseContainer>(errorResponseContainer, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
