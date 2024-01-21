package com.spring.project.Exception;

public class EmailNotAvailableException extends RuntimeException{

    public EmailNotAvailableException(String message){
        super(message);
    }
}
