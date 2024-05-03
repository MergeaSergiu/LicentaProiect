package com.spring.project.controller;

import com.spring.project.dto.CourtDetailsResponse;
import com.spring.project.service.CourtDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path ="project/api/v1/details")
@AllArgsConstructor
public class CourtDetailsController {

    @Autowired
    private final CourtDetailsService courtDetailsService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<CourtDetailsResponse>> getCourtsDetails(){
        List<CourtDetailsResponse> courtDetailsResponseList = courtDetailsService.getDetails();
        return new ResponseEntity<>(courtDetailsResponseList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{court}")
    public ResponseEntity<CourtDetailsResponse> getCourtResponse(@PathVariable("court") String court){
        CourtDetailsResponse courtDetailsResponse = courtDetailsService.getCourtDetails(court);
        return new ResponseEntity<>(courtDetailsResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{courtId}")
    public ResponseEntity<Void> updateCourtDetails(@PathVariable("courtId") Long courtId, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime){
        Integer startTimeCourt = Integer.parseInt(startTime);
        Integer endTimeCourt = Integer.parseInt(endTime);
        courtDetailsService.updateCourtDetails(courtId,startTimeCourt,endTimeCourt);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
