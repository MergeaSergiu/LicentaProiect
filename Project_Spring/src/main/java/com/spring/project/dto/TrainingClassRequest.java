package com.spring.project.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@Getter
@Setter
public class TrainingClassRequest {

    @NotBlank
    private String className;
    @NotNull
    private int duration;
    @NotBlank
    private String startTime;
    @NotBlank
    private String intensity;
    @NotBlank
    private String localDate;
    @NotNull
    private Integer trainerId;
}
