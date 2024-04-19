package com.spring.project.Exception;

public class CustomExpiredTokenException extends RuntimeException{

    public CustomExpiredTokenException(String message){
        super(message);
    }
}
