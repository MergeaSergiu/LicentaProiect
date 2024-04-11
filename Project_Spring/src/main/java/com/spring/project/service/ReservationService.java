package com.spring.project.service;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;

import java.util.List;

public interface ReservationService {

    void saveReservation(ReservationRequest reservationRequest, String authorization);

    List<ReservationResponse> getAllReservations();

    List<ReservationResponse> getAllUserReservations(String authorization);

    void deleteReservation(Long id, String authorization);

    void deleteReservationsForUser(Long id);

    List<ReservationResponse> getReservationsByCourt(String court);
}
