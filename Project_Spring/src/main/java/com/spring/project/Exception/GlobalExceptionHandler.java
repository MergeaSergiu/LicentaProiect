package com.spring.project.Exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponseContainer> handleEntityExistsException(EntityExistsException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseContainer> handleIllegalArgumentException(IllegalArgumentException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();
        String errorMessage = ex.getMessage();
        int startIndex = errorMessage.indexOf("messageTemplate='") + "messageTemplate='".length();
        int endIndex = errorMessage.indexOf("'", startIndex);
        String message = errorMessage.substring(startIndex, endIndex);
        errorResponseContainer.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponseContainer.setErrorMessage(message);
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseContainer> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponseContainer.setErrorMessage("Can not process the request.One field may be invalid.");
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseContainer> handleConstraintViolationException(ConstraintViolationException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();
        String errorMessage = ex.getMessage();
        int startIndex = errorMessage.indexOf("messageTemplate='") + "messageTemplate='".length();
        int endIndex = errorMessage.indexOf("'", startIndex);
        String message = errorMessage.substring(startIndex, endIndex);
        errorResponseContainer.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponseContainer.setErrorMessage(message);
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseContainer> handleAuthenticationException(AuthenticationException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponseContainer.setErrorMessage("Email or Password are invalid");
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponseContainer> handleExpiredJwtException(ExpiredJwtException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomExpiredTokenException.class)
    public ResponseEntity<ErrorResponseContainer> handleCustomExpiredTokenException(CustomExpiredTokenException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.FORBIDDEN.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResetPasswordException.class)
    public ResponseEntity<ErrorResponseContainer> handleResetPasswordException(ResetPasswordException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.GONE.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.GONE);
    }

    @ExceptionHandler(ConfirmAccountException.class)
    public ResponseEntity<ErrorResponseContainer> handleConfirmAccountException(ConfirmAccountException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.GONE.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.GONE);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseContainer> handleEntityNotFoundException(EntityNotFoundException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseContainer> handleIllegalStateException(IllegalStateException ex){
        ErrorResponseContainer errorResponseContainer = new ErrorResponseContainer();

        errorResponseContainer.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponseContainer.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponseContainer, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
