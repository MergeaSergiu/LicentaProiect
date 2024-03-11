package com.spring.project.service;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.Reservation;

import java.util.List;

public interface ReservationService {

    void saveReservation(ReservationRequest reservationRequest);

    void sendEmails();

    List<Reservation> getAllReservations();

    List<Reservation> getAllClientReservations(Integer id);

    void deleteReservation(Integer id);

    void deleteReservationsForUser(Integer id);

    List<ReservationResponse> getReservationsByCourt(String court);
}
