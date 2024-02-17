package com.spring.project.service.impl;

import com.spring.project.model.CourtReservation;
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

    public CourtReservation save(CourtReservation courtReservation){
        return reservationRepository.save(courtReservation);
    }

    public List<CourtReservation> getReservationWithCurrentDay(){
            LocalDate localDate = LocalDate.now();
            return reservationRepository.findByLocalDateCurrentDate(localDate);
    }


    public void deleteReservation(String email,String hourSchedule, LocalDate localDate, String court){
        reservationRepository.deleteReservation(email,hourSchedule, localDate, court);
    }

    public List<CourtReservation> getReservationsByCourt(String court) {
       return reservationRepository.findByCourt(court);
    }
}
