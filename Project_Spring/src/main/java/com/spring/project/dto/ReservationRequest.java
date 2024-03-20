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
public class ReservationRequest {

    String localDate;
    String hourSchedule;
    String court;
    LocalDate currentDate;
}
