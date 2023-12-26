package com.spring.project.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateClassRequest {

    @NotBlank(message = "Please set a name for the training Class")
    @NotNull
    private String className;

    @NotNull
    private int duration;

    @NotNull(message = "Please set an intensity for the training")
    @NotBlank
    private String intensity;

    @NotNull(message = "Please set a date for the event")
    private LocalDate localDate;

    @NotNull(message = "Plase add a trainer for the training class")
    @NotBlank
    private String trainerEmail;
}
