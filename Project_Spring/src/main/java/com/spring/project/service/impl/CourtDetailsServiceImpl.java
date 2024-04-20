package com.spring.project.service.impl;


import com.spring.project.dto.CourtDetailsResponse;
import com.spring.project.model.Court;
import com.spring.project.model.CourtDetails;
import com.spring.project.repository.CourtDetailsRepository;
import com.spring.project.service.CourtDetailsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourtDetailsServiceImpl implements CourtDetailsService {

    private final CourtDetailsRepository courtDetailsRepository;
    @Override
    public CourtDetailsResponse getDetails(String courtName) {
        Court court = Court.valueOf(courtName.toUpperCase());
        CourtDetails courtDetails = courtDetailsRepository.findByCourt(court);
        if(courtDetails == null){
            throw new EntityNotFoundException("Court does not exist");
        }
        return CourtDetailsResponse.builder()
                .court(courtName)
                .startTime(courtDetails.getStartTime())
                .endTime(courtDetails.getEndTime())
                .build();
    }
}
