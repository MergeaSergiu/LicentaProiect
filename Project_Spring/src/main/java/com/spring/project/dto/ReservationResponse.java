package com.spring.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    Integer id;
    String reservationDate;
    String hourSchedule;
    String clientEmail;
    String court;
}
