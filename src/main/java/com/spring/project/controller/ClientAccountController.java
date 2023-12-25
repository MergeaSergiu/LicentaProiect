package com.spring.project.controller;

import com.spring.project.dto.ReservationResponse;
import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.model.FotballInsideReservation;
import com.spring.project.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping(path = "/project/api/user")
public class ClientAccountController {

    @Autowired
    private final UserAccountService userAccountService;
    @GetMapping("/accountReservations")
    public ResponseEntity<List<ReservationResponse>> getReservationHistory(){
            List<FotballInsideReservation> reservationResponses = userAccountService.getAllClientReservation();
            List<ReservationResponse> reservations = new ArrayList<>();
            for(FotballInsideReservation fotballInsideReservation: reservationResponses){
                ReservationResponse reservationResponse = new ReservationResponse(fotballInsideReservation.getLocalDate(), fotballInsideReservation.getHourSchedule(), fotballInsideReservation.getEmail());
                reservations.add(reservationResponse);
            }
            return ResponseEntity.ok(reservations);
    }

}
