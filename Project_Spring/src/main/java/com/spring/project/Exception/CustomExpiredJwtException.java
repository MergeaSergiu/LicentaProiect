package com.spring.project.Exception;

public class CustomExpiredJwtException extends RuntimeException{

    public CustomExpiredJwtException(String message){
        super(message);
    }
}
