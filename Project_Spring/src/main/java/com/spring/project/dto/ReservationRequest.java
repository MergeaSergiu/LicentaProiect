package com.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Data
@Builder
@Getter
@Setter
public class ReservationRequest {

    @NotBlank
    String localDate;
    @NotBlank
    String hourSchedule;
    @NotBlank
    String court;
    @NotNull
    LocalDate currentDate;
}
