package com.spring.project.service.impl;


import com.spring.project.dto.CourtDetailsResponse;
import com.spring.project.mapper.CourtDetailsMapper;
import com.spring.project.model.Court;
import com.spring.project.model.CourtDetails;
import com.spring.project.repository.CourtDetailsRepository;
import com.spring.project.repository.ReservationRepository;
import com.spring.project.service.CourtDetailsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourtDetailsServiceImpl implements CourtDetailsService {

    private final CourtDetailsRepository courtDetailsRepository;
    private final CourtDetailsMapper courtDetailsMapper;
    private final ReservationRepository reservationRepository;

    @Override
    public List<CourtDetailsResponse> getDetails() {
        List<CourtDetails> courtDetails = courtDetailsRepository.findAll();
        return courtDetails.stream()
                .map(courtDetailsMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourtDetailsResponse getCourtDetails(String courtName) {
        Court court = Court.valueOf(courtName.toUpperCase());
        CourtDetails courtDetails = courtDetailsRepository.findByCourt(court);
        if(courtDetails == null){
            throw new EntityNotFoundException("Court does not exist");
        }
        return courtDetailsMapper.convertToDto(courtDetails);
    }

    @Override
    public void updateCourtDetails(Long courtId, Integer startTime, Integer endTime) {
        CourtDetails courtDetails = courtDetailsRepository.findById(courtId).orElse(null);
        if(courtDetails == null){
            throw new EntityNotFoundException("There is no court to update the details");
        }
        if(startTime.compareTo(endTime) >= 0){
            throw new EntityNotFoundException("Wrong timeSlots used.");
        }

        reservationRepository.deleteAllReservationBasedOnStartAndEndTime(startTime, endTime);

        courtDetails.setStartTime(startTime);
        courtDetails.setEndTime(endTime);
        courtDetailsRepository.save(courtDetails);
    }
}
