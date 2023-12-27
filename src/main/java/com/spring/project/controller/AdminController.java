package com.spring.project.controller;

import com.spring.project.dto.*;
import com.spring.project.model.Client;
import com.spring.project.model.FotballInsideReservation;
import com.spring.project.service.AdminService;
import com.spring.project.service.impl.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "project/api/admin")
public class AdminController {

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final ClientService clientService;

    @GetMapping("/secured")
    public ResponseEntity<String> getAdmin(){
        return ResponseEntity.ok("Ai accesat pagina adminului");
    }

    @PostMapping("/createTrainer")
    public ResponseEntity<TrainerResponse> createTrainer(@Valid @RequestBody TrainerRequest trainerRequest) {
        return ResponseEntity.ok(adminService.createTrainer(trainerRequest));
    }

    @GetMapping("/users")
    public ResponseEntity<List<ClientResponse>> getAllUsers(){

        List<ClientResponse> clientResponses = adminService.getAllClients();
//        for(Client client: clients){
//            ClientResponse clientResponse = new ClientResponse();
//            clientResponse.setFirstName(client.getFirstName());
//            clientResponse.setLastName(client.getLastName());
//            clientResponse.setEmail(client.getEmail());
//            clientResponses.add(clientResponse);
//        }
            return ResponseEntity.ok(clientResponses);
    }



    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations(){
        List<ReservationResponse> reservations = adminService.getAllReservations();
//        List<ReservationResponse> reservationResponses = new ArrayList<>();
//        for(FotballInsideReservation reservation : reservations){
//            ReservationResponse reservationResponse = new ReservationResponse(reservation.getLocalDate(),reservation.getHourSchedule(),reservation.getEmail());
//            reservationResponses.add(reservationResponse);
//        }
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/createSubscription")
    public ResponseEntity<String> createSubscription(@RequestBody @Valid CreateSubscriptionRequest createSubscriptionRequest){
        adminService.createSubscription(createSubscriptionRequest);
        return new ResponseEntity<>("Subscription created successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/updateSubscription/{id}")
    public ResponseEntity<String> updateSubscription(@PathVariable("id") Integer id, @RequestBody Map<String, Object> fields){
        adminService.updateSubscription(id, fields);
        return ResponseEntity.ok("Subscription data was update");
    }

    @DeleteMapping("/deleteSubscription/{id}")
    public ResponseEntity<String> deleteSubscription(@PathVariable("id") Integer id){
        adminService.deleteSubscription(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/createClass")
    public ResponseEntity<String> createTrainingClass(@RequestBody @Valid CreateClassRequest classRequest) {
            if (clientService.findClientByEmail(classRequest.getTrainerEmail()) != null) {
                if (clientService.findClientByEmail(classRequest.getTrainerEmail()).getClientRole().toString().equals("TRAINER")) {
                    adminService.createTrainingClass(classRequest);
                    return ResponseEntity.status(HttpStatus.CREATED).build();
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PatchMapping("/updateClass/{id}")
    public ResponseEntity<String> updateTrainingClass(@PathVariable("id") Integer id, @RequestBody Map<String, Object> fields){
        adminService.updateTrainingClass(id,fields);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/deleteClass/{id}")
    public ResponseEntity<String> deleteTrainingClass(@PathVariable("id") Integer id){
        adminService.deleteTrainingClass(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
