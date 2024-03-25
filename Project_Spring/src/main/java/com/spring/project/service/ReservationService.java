package com.spring.project.service;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.Reservation;

import java.util.List;

public interface ReservationService {

    void saveReservation(ReservationRequest reservationRequest);

    void sendEmails();

    List<Reservation> getAllReservations();

    List<Reservation> getAllClientReservations(Long id);

    void deleteReservation(Long id);

    void deleteReservationsForUser(Long id);

    List<ReservationResponse> getReservationsByCourt(String court);
}
