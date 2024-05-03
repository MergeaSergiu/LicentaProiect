package com.spring.project.service;

import com.spring.project.dto.CourtDetailsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourtDetailsService {

     List<CourtDetailsResponse> getDetails();
    CourtDetailsResponse getCourtDetails(String court);
    void updateCourtDetails(Long courtId, Integer startTime, Integer endTime);


}
