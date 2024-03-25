package com.spring.project.controller;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.service.AdminService;
import com.spring.project.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(path = "project/api/v1/reservations")
@AllArgsConstructor
public class ReservationController {

    @Autowired
    private final ReservationService reservationService;

    @Autowired
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations(){
        return ResponseEntity.ok(adminService.getAllReservations());
    }
    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody ReservationRequest reservationRequest){
        reservationService.saveReservation(reservationRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long reservationId){
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{court}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByCourt(@PathVariable("court") String court){
        List<ReservationResponse> reservationResponseList = reservationService.getReservationsByCourt(court);
        return ResponseEntity.ok(reservationResponseList);
    }

}
