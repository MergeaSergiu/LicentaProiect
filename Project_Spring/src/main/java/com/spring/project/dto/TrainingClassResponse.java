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

    private Integer id;
    private String className;
    private Integer duration;
    private String intensity;
    private String localDate;
    private Integer trainerId;
    private String lastName;
    private String firstName;

}
