package com.spring.project.dto;


import lombok.*;

@Data
@Builder
@Getter
@Setter
public class TrainingClassRequest {

    private String className;
    private int duration;
    private String startTime;
    private String intensity;
    private String localDate;
    private Integer trainerId;
}
