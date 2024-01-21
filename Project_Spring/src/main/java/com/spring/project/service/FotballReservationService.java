package com.spring.project.service;

import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.CourtReservation;

import java.time.LocalDate;
import java.util.List;

public interface FotballReservationService {

    CourtReservation save(CourtReservation courtReservation);

    List<CourtReservation> getReservationWithCurrentDay();

    void deleteReservation(String email, String hourSchedule, LocalDate localDate, String court);

    List<CourtReservation> getReservationsByCourt(String court);

}
