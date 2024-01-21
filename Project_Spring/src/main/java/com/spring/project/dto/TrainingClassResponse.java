package com.spring.project.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
public class TrainingClassResponse {

    private String className;
    private Integer duration;
    private String intensity;
    private LocalDate localDate;
    private String trainerEmail;

}
