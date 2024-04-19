package com.spring.project.mapper;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationRequestByAdmin;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.Court;
import com.spring.project.model.User;
import com.spring.project.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Component
public class ReservationMapper {

    public ReservationResponse convertToDto(Reservation reservation){
        return ReservationResponse.builder()
                .id(Math.toIntExact(reservation.getId()))
                .reservationDate(reservation.getReservationDate().toString())
                .hourSchedule(reservation.getHourSchedule())
                .court(reservation.getCourt().toString())
                .clientEmail(reservation.getUser().getEmail())
                .build();
    }

    public Reservation convertFromDto(ReservationRequest reservationRequest, User user){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return Reservation.builder()
                .reservationDate(LocalDate.parse(reservationRequest.getLocalDate(), formatter))
                .hourSchedule(reservationRequest.getHourSchedule())
                .court(Court.valueOf(reservationRequest.getCourt()))
                .user(user)
                .build();
    }

    public Reservation convertDtoAdminReservation(ReservationRequestByAdmin reservationRequestByAdmin, User user){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return Reservation.builder()
                .reservationDate(LocalDate.parse(reservationRequestByAdmin.getLocalDate(), formatter))
                .hourSchedule(reservationRequestByAdmin.getHourSchedule())
                .court(Court.valueOf(reservationRequestByAdmin.getCourt()))
                .user(user)
                .build();
    }

}
