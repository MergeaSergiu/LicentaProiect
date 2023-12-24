package com.spring.project.service;

import com.spring.project.model.FotballInsideReservation;
import com.spring.project.repository.FotballInsideReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class FotballReservationService {

    private final FotballInsideReservationRepository fotballInsideReservationRepository;

    public FotballInsideReservation save(FotballInsideReservation fotballInsideReservation){
        return fotballInsideReservationRepository.save(fotballInsideReservation);
    }

    public List<FotballInsideReservation> getReservationWithCurrentDay(){
            LocalDate localDate = LocalDate.now();
            return fotballInsideReservationRepository.findByLocalDateCurrentDate(localDate);
    }

    public void deleteReservation(String email, String hourSchedule, LocalDate localDate){
        fotballInsideReservationRepository.deleteReservation(email,hourSchedule, localDate);
    }
}
