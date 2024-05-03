package com.spring.project.mapper;

import com.spring.project.dto.CourtDetailsResponse;
import com.spring.project.model.CourtDetails;
import org.springframework.stereotype.Component;

@Component
public class CourtDetailsMapper {

    public CourtDetailsResponse convertToDto(CourtDetails courtDetails){
        String court = switch (courtDetails.getCourt()) {
            case FOOTBALL -> "FOOTBALL";
            case BASKETBALL -> "BASKETBALL";
            case TENNIS -> "TENNIS";
        };

        return CourtDetailsResponse.builder()
                .id(courtDetails.getId())
                .court(court)
                .startTime(courtDetails.getStartTime())
                .endTime(courtDetails.getEndTime())
                .build();
    }
}
