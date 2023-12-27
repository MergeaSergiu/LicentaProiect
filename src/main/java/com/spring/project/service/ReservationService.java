package com.spring.project.service;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.FotballInsideReservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    ReservationResponse saveReservation(ReservationRequest reservationRequest);

    void sendEmails();

    List<FotballInsideReservation> getAllReservations();

    List<FotballInsideReservation> getAllClientReservation(String clientEmail);

    void deleteReservation(String hourSchedule, LocalDate localDate);
}
