package com.spring.project.service.impl;

import com.spring.project.Exception.ClientNotFoundException;
import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.email.EmailSender;
import com.spring.project.mapper.ReservationMapper;
import com.spring.project.model.Client;
import com.spring.project.model.Reservation;
import com.spring.project.repository.ReservationRepository;
import com.spring.project.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final FotballReservationServiceImpl fotballReservationServiceImpl;
    private final ReservationRepository reservationRepository;
    private final EmailSender emailSender;
    private final ReservationMapper reservationMapper;
    private final ClientService clientService;

    public void saveReservation(ReservationRequest reservationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            Client user = clientService.findClientByEmail(authentication.getName());
            if (user != null) {
                Reservation reservation = reservationMapper.convertFromDto(reservationRequest,user);
                fotballReservationServiceImpl.save(reservation);
                String emailTemplate = loadEmailTemplateFromResource("reservationResponseEmail.html");
                emailTemplate = emailTemplate.replace("${email}", authentication.getName());
                emailTemplate = emailTemplate.replace("${hourSchedule}", reservationRequest.getHourSchedule());
                emailTemplate = emailTemplate.replace("${dateTime}", reservationRequest.getLocalDate());
                emailSender.send(authentication.getName(), emailTemplate, "Thank you for your reservation");
            }else {
                throw new ClientNotFoundException("User does not exist");
            }
        }

    }

    @Override
    public void sendEmails() {

    }

    private String loadEmailTemplateFromResource(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    @Override
    public List<Reservation> getAllClientReservations(Integer id) {
        return reservationRepository.findByuser_Id(id);
    }

    @Override
    public void deleteReservation(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        fotballReservationServiceImpl.deleteReservation(id);
        String emailTemplate = loadEmailTemplateFromResource("deleteReservationEmail.html");
        emailTemplate = emailTemplate.replace("${email}", authentication.getName());
        emailSender.send(authentication.getName(), emailTemplate, "Reservation was deleted");
    }

    @Override
    public void deleteReservationsForUser(Integer id) {
        reservationRepository.deleteAllByuser_Id(id);
    }

    @Override
    public List<ReservationResponse> getReservationsByCourt(String court) {
            List<Reservation> reservations = fotballReservationServiceImpl.getReservationsByCourt(court);
            if(reservations != null) {
                return reservations.stream()
                        .map(courtReservation -> reservationMapper.convertToDto(courtReservation)).collect(Collectors.toList());
            }else {
                return new ArrayList<>();
            }
    }

}
