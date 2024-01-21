package com.spring.project.service;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.CourtReservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    void saveReservation(ReservationRequest reservationRequest);

    void sendEmails();

    List<CourtReservation> getAllClientReservation(String clientEmail);

    void deleteReservation(String startTime, String endTime, LocalDate localDate, String court);

    List<ReservationResponse> getReservationsByCourt(String court);
}
