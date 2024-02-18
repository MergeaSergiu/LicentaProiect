package com.spring.project.controller;

import com.spring.project.dto.ReservationResponse;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.service.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping(path = "/project/api/user")
public class ClientAccountController {

    @Autowired
    private final UserAccountService userAccountService;
    @GetMapping("/accountReservations")
    public ResponseEntity<List<ReservationResponse>> getReservationHistory(){
        List<ReservationResponse> reservations = userAccountService.getAllClientReservation();
            return ResponseEntity.ok(reservations);
    }

    @GetMapping("/getEnrollClasses")
    public ResponseEntity<List<TrainingClassResponse>> getEnrollClassesForUser(){
        return ResponseEntity.ok(userAccountService.getEnrollClasses());
    }

}