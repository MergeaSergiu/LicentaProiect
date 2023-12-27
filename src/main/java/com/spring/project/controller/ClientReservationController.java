package com.spring.project.controller;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.service.ReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping(path = "project/api/user")
@AllArgsConstructor
public class ClientReservationController {

    @Autowired
    private final ReservationService reservationService;
    @PostMapping("/createReservation")
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest reservationRequest){
            return ResponseEntity.ok(reservationService.saveReservation(reservationRequest));
    }

    @DeleteMapping("/deleteReservation")
    public ResponseEntity<String> deleteReservation(@RequestParam("hourSchedule") String hourSchedule, @RequestParam("localDate") LocalDate localDate){
        reservationService.deleteReservation(hourSchedule, localDate);
        return ResponseEntity.ok("Reservation was Deleted");
    }

}
