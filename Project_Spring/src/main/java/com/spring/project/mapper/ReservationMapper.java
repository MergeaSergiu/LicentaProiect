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
        String hourSchedule = reservation.getStartTime() + "-" + reservation.getEndTime();
        return ReservationResponse.builder()
                .id(Math.toIntExact(reservation.getId()))
                .reservationDate(reservation.getReservationDate().toString())
                .hourSchedule(hourSchedule)
                .court(reservation.getCourt().toString())
                .userName(reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName())
                .build();
    }

    public Reservation convertFromDto(ReservationRequest reservationRequest, User user, Integer startTime, Integer endTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return Reservation.builder()
                .reservationDate(LocalDate.parse(reservationRequest.getLocalDate(), formatter))
                .startTime(startTime)
                .endTime(endTime)
                .court(Court.valueOf(reservationRequest.getCourt()))
                .user(user)
                .build();
    }

    public Reservation convertDtoAdminReservation(ReservationRequestByAdmin reservationRequestByAdmin, User user, Integer startTime, Integer endTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return Reservation.builder()
                .reservationDate(LocalDate.parse(reservationRequestByAdmin.getLocalDate(), formatter))
                .startTime(startTime)
                .endTime(endTime)
                .court(Court.valueOf(reservationRequestByAdmin.getCourt()))
                .user(user)
                .build();
    }

}
