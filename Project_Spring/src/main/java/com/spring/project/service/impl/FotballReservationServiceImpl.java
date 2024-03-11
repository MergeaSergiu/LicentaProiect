package com.spring.project.service.impl;

import com.spring.project.model.Reservation;
import com.spring.project.repository.ReservationRepository;
import com.spring.project.service.FotballReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class FotballReservationServiceImpl implements FotballReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation save(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationWithCurrentDay(){
            LocalDate localDate = LocalDate.now();
            return reservationRepository.findByLocalDateCurrentDate(localDate);
    }


    @Override
    public void deleteReservation(Integer id){
        reservationRepository.deleteById(id);
    }

    public List<Reservation> getReservationsByCourt(String court) {
       return reservationRepository.findByCourt(court);
    }
}
