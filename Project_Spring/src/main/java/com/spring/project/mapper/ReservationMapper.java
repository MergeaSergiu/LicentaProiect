package com.spring.project.mapper;

import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.CourtReservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponse convertToDto(CourtReservation reservation){
        return ReservationResponse.builder()
                .id(reservation.getId())
                .localDate(reservation.getLocalDate())
                .hourSchedule(reservation.getHourSchedule())
                .court(reservation.getCourt())
                .clientEmail(reservation.getEmail())
                .build();
    }

}
