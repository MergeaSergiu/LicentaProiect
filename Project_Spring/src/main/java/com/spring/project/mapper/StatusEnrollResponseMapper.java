package com.spring.project.mapper;

import com.spring.project.dto.StatusEnrollResponse;
import org.springframework.stereotype.Component;

@Component
public class StatusEnrollResponseMapper {

    public StatusEnrollResponse createStatusEnrollResponse(String message){
        return StatusEnrollResponse
                .builder()
                .status(message)
                .build();
    }
}
