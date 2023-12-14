package com.spring.project.service;

import com.spring.project.model.FotballInsideReservation;
import com.spring.project.repository.FotballInsideReservationRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
}
