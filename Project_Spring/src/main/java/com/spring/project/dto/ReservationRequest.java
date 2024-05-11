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

    private String localDate;
    private String hourSchedule;
    private String court;

}
