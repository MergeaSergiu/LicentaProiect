package com.spring.project.service;

import com.spring.project.model.FotballInsideReservation;

import java.time.LocalDate;
import java.util.List;

public interface FotballReservationService {

    FotballInsideReservation save(FotballInsideReservation fotballInsideReservation);

    List<FotballInsideReservation> getReservationWithCurrentDay();

    void deleteReservation(String email, String hourSchedule, LocalDate localDate);

}
