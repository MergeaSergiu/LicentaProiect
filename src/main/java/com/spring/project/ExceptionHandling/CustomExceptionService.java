package com.spring.project.ExceptionHandling;

import org.springframework.http.HttpStatus;

public class CustomExceptionService extends RuntimeException{

    private final HttpStatus httpStatus;

    public CustomExceptionService(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CustomExceptionService(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
