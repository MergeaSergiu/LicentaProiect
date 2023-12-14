package com.spring.project.controller;

import com.spring.project.dto.ClientResponse;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.dto.TrainerRequest;
import com.spring.project.dto.TrainerResponse;
import com.spring.project.model.Client;
import com.spring.project.model.FotballInsideReservation;
import com.spring.project.service.AdminService;
import com.spring.project.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "project/api/admin")
public class AdminController {

    private final AdminService adminService;
    @GetMapping("/secured")
    public ResponseEntity<String> getAdmin(){
        return ResponseEntity.ok("Ai accesat pagina adminului");
    }

    @PostMapping("/createTrainer")
    public ResponseEntity<TrainerResponse> createTrainer(HttpServletRequest request, @RequestBody TrainerRequest trainerRequest) {
        return ResponseEntity.ok(adminService.createTrainer(request,trainerRequest));
    }

    @GetMapping("/users")
    public ResponseEntity<List<ClientResponse>> getAllUsers(HttpServletRequest request){

        List<Client> clients = adminService.getAllClients(request);
        List<ClientResponse> clientResponses = new ArrayList<>();
        for(Client client: clients){
            ClientResponse clientResponse = new ClientResponse();
            clientResponse.setFirstName(client.getFirstName());
            clientResponse.setLastName(client.getLastName());
            clientResponse.setEmail(client.getEmail());
            clientResponses.add(clientResponse);
        }
            return ResponseEntity.ok(clientResponses);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations(HttpServletRequest request){
        List<FotballInsideReservation> reservations = adminService.getAllReservations(request);
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for(FotballInsideReservation reservation : reservations){
            ReservationResponse reservationResponse = new ReservationResponse(reservation.getLocalDate(),reservation.getHourSchedule(),reservation.getEmail());
            reservationResponses.add(reservationResponse);
        }
        return ResponseEntity.ok(reservationResponses);
    }
}
