package com.spring.project.ExceptionHandling;

import org.springframework.http.HttpStatus;

public class CustomExceptionController extends RuntimeException {

    private final HttpStatus httpStatus;

    public CustomExceptionController(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CustomExceptionController(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
