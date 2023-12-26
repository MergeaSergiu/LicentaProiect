package com.spring.project.email;

public interface EmailSender {

    void send(String to, String email);

    void sendReservationResponse(String to, String email);

    void sendRemainder(String to, String email);

    void sendReservationDeleteResponse(String to, String email);

    void sendCreateClassResponse(String to, String email);

    void sendEnrollClassResponse(String to, String email);
}
