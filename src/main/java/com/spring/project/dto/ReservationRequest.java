package com.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "You must select a localDate")
    LocalDate localDate;

    @NotBlank(message = "You must select an hour Schedule")
    String hourSchedule;
}
