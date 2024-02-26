package com.spring.project.controller;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.service.ReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(path = "project/api/user")
@AllArgsConstructor
public class ClientReservationController {

    @Autowired
    private final ReservationService reservationService;
    @PostMapping("/createReservation")
    public ResponseEntity<Void> createReservation(@RequestBody ReservationRequest reservationRequest){
            reservationService.saveReservation(reservationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/deleteReservation")
    public ResponseEntity<Void> deleteReservation(@RequestParam("id") Integer id){
        reservationService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/getReservationsByCourt")
    public ResponseEntity<List<ReservationResponse>> getReservationsByCourt(@RequestParam("court") String court){
        List<ReservationResponse> reservationResponseList = reservationService.getReservationsByCourt(court);
        return ResponseEntity.ok(reservationResponseList);
    }

}
