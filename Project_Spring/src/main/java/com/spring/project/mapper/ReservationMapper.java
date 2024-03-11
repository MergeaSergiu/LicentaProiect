package com.spring.project.mapper;

import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponse convertToDto(Reservation reservation){
        return ReservationResponse.builder()
                .id(reservation.getId())
                .localDate(reservation.getLocalDate())
                .hourSchedule(reservation.getHourSchedule())
                .court(reservation.getCourt())
                .clientEmail(reservation.getUser().getEmail())
                .build();
    }

}
