package com.spring.project.controller;

import com.spring.project.dto.CourtDetailsResponse;
import com.spring.project.service.CourtDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path ="project/api/v1/details")
@AllArgsConstructor
public class CourtDetailsController {

    @Autowired
    private final CourtDetailsService courtDetailsService;

    @GetMapping("/{court}")
    public ResponseEntity<CourtDetailsResponse> getCourtResponse(@PathVariable("court") String court){
        CourtDetailsResponse courtDetailsResponse = courtDetailsService.getDetails(court);
        return new ResponseEntity<>(courtDetailsResponse, HttpStatus.OK);
    }
}
