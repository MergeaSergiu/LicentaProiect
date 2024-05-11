package com.spring.project.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ReservationRequestByAdmin {

    private Long userId;
    private String localDate;
    private String hourSchedule;
    private String court;
}
