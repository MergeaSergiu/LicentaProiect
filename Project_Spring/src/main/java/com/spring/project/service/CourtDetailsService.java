package com.spring.project.service;

import com.spring.project.dto.CourtDetailsResponse;
import org.springframework.stereotype.Service;

@Service
public interface CourtDetailsService {

    public CourtDetailsResponse getDetails(String court);

}
