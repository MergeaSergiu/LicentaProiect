package com.spring.project.email;

public interface EmailSender {
    void send(String to, String email, String subject);

}
