package com.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ReservationRequestByAdmin {

    @NotBlank
    Long userId;
    @NotBlank
    String localDate;
    @NotBlank
    String hourSchedule;
    @NotBlank
    String court;
}
