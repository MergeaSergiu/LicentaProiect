package com.spring.project.service;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.CourtReservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    void saveReservation(ReservationRequest reservationRequest);

    void sendEmails();

    List<CourtReservation> getAllReservations();

    List<CourtReservation> getAllClientReservation(String clientEmail);

    void deleteReservation(Integer id);

    List<ReservationResponse> getReservationsByCourt(String court);
}
