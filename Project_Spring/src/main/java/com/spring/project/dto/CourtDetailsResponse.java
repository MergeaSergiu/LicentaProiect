package com.spring.project.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourtDetailsResponse {

    Long id;
    String court;
    Integer startTime;
    Integer endTime;
}