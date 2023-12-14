package com.spring.project.controller;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "project/api/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    @PostMapping("/createReservation")
    public ResponseEntity<ReservationResponse> createReservation(HttpServletRequest request, @Valid @RequestBody ReservationRequest reservationRequest){
            return ResponseEntity.ok(reservationService.saveReservation(request,reservationRequest));
    }
}
