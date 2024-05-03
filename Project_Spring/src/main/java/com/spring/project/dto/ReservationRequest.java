package com.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Builder
@Getter
@Setter
public class ReservationRequest {

    @NotBlank
    private String localDate;

    @NotBlank
    private String hourSchedule;

    @NotBlank
    private String court;

}
