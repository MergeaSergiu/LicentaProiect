package com.spring.project.mapper;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.Client;
import com.spring.project.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Component
public class ReservationMapper {

    public ReservationResponse convertToDto(Reservation reservation){
        return ReservationResponse.builder()
                .id(reservation.getId())
                .reservationDate(reservation.getReservationDate().toString())
                .hourSchedule(reservation.getHourSchedule())
                .court(reservation.getCourt())
                .clientEmail(reservation.getUser().getEmail())
                .build();
    }

    public Reservation convertFromDto(ReservationRequest reservationRequest, Client user){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return Reservation.builder()
                .reservationDate(LocalDate.parse(reservationRequest.getLocalDate(), formatter))
                .hourSchedule(reservationRequest.getHourSchedule())
                .court(reservationRequest.getCourt())
                .user(user)
                .build();
    }

}
