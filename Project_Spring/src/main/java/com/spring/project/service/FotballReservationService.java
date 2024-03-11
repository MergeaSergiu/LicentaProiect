package com.spring.project.service;

import com.spring.project.model.Reservation;

import java.util.List;

public interface FotballReservationService {

    Reservation save(Reservation reservation);

    List<Reservation> getReservationWithCurrentDay();

    void deleteReservation(Integer id);

    List<Reservation> getReservationsByCourt(String court);

}
