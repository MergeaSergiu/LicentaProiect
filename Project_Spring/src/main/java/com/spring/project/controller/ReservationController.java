package com.spring.project.controller;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationRequestByAdmin;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(path = "project/api/v1/reservations")
@AllArgsConstructor
public class ReservationController {

    @Autowired
    private final ReservationService reservationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations(){
        List<ReservationResponse> reservationResponseList = reservationService.getAllReservations();
        return new ResponseEntity<>(reservationResponseList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<List<ReservationResponse>> getUserReservations(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        List<ReservationResponse> reservations = reservationService.getAllUserReservations(authorization);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<Void> createReservationByAdmin(@RequestBody ReservationRequestByAdmin reservationRequestByAdmin){
        reservationService.saveReservationByAdmin(reservationRequestByAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody ReservationRequest reservationRequest){
        reservationService.saveReservation(reservationRequest, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable("reservationId") Long reservationId){
        reservationService.deleteReservation(reservationId, authorization);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{court}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByCourt(@PathVariable("court") String court){
        List<ReservationResponse> reservationResponseList = reservationService.getReservationsByCourt(court);
        return new ResponseEntity<>(reservationResponseList, HttpStatus.OK);
    }

}
