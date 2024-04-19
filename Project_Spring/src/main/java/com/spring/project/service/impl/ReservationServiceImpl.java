package com.spring.project.service.impl;

import com.spring.project.Exception.CreateReservationException;
import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationRequestByAdmin;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.email.EmailSender;
import com.spring.project.mapper.ReservationMapper;
import com.spring.project.model.Court;
import com.spring.project.model.User;
import com.spring.project.model.Reservation;
import com.spring.project.repository.ClientRepository;
import com.spring.project.repository.ReservationRepository;
import com.spring.project.service.ReservationService;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final EmailSender emailSender;
    private final ReservationMapper reservationMapper;
    private final ClientRepository clientRepository;
    private final UtilMethods utilMethods;

    public void saveReservation(ReservationRequest reservationRequest, String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = clientRepository.findByEmail(username).orElse(null);
            if (user == null) {
                throw new EntityNotFoundException("User does not exist");
            }
            boolean existingReservation = reservationRepository.findAll().stream()
                    .anyMatch(reservation -> reservation.getReservationDate().toString().equals(reservationRequest.getLocalDate().toString())
                            && reservation.getHourSchedule().equals(reservationRequest.getHourSchedule())
                            && reservation.getCourt().equals(reservationRequest.getCourt()));
            if(existingReservation){
                throw new CreateReservationException("There is a reservation at the same moment created");
            }
            List<Reservation> reservationsForCurrentDayForUser = reservationRepository.findAllByUser_IdAndReservationMadeDate(user.getId(), LocalDate.now());
            if(reservationsForCurrentDayForUser.size() >=3){
                throw new CreateReservationException("You reached the reservations limit per day");
            }
            Reservation reservation = reservationMapper.convertFromDto(reservationRequest, user);
            reservationRepository.save(reservation);
            String emailTemplate = utilMethods.loadEmailTemplateFromResource("reservationResponseEmail.html");
            emailTemplate = emailTemplate.replace("${email}", username);
            emailTemplate = emailTemplate.replace("${hourSchedule}", reservationRequest.getHourSchedule());
            emailTemplate = emailTemplate.replace("${dateTime}", reservationRequest.getLocalDate());
            emailSender.send(username, emailTemplate, "Thank you for your reservation");
    }

    @Override
    public void saveReservationByAdmin(ReservationRequestByAdmin reservationRequestByAdmin) {
        User user = clientRepository.findById(reservationRequestByAdmin.getUserId()).orElse(null);
        if(user == null){
            throw new EntityNotFoundException("User does not exist");
        }
        boolean existingReservation = reservationRepository.findAll().stream()
                .anyMatch(reservation -> reservation.getReservationDate().toString().equals(reservationRequestByAdmin.getLocalDate())
                        && reservation.getHourSchedule().equals(reservationRequestByAdmin.getHourSchedule())
                        && reservation.getCourt().equals(reservationRequestByAdmin.getCourt()));
        if(existingReservation){
            throw new CreateReservationException("There is a reservation at the same moment created");
        }

        List<Reservation> reservationsForCurrentDayForUser = reservationRepository.findAllByUser_IdAndReservationMadeDate(user.getId(), LocalDate.now());
        if(reservationsForCurrentDayForUser.size() >=3){
            throw new CreateReservationException("User reached the limit of 3 reservations per day");
        }

        Reservation reservation = reservationMapper.convertDtoAdminReservation(reservationRequestByAdmin, user);
        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAllByOrderByReservationDateAsc().stream()
                .map(reservationMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> getAllUserReservations(String authorization) {
        String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        User user = clientRepository.findByEmail(username).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User does not exist");
        }

        return reservationRepository.findAllByUser_IdOrderByReservationDateAsc(user.getId()).stream().map(reservationMapper::convertToDto).collect(Collectors.toList());
    }
    @Override
    public void deleteReservation(Long id, String authorization) {
        String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        User user = clientRepository.findByEmail(username).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User does not exist");
        }
            Reservation reservation = reservationRepository.findById(id).orElse(null);
            if(reservation == null) {
                throw new CreateReservationException("Reservation does not exist");
            }
            if(reservation.getReservationDate().isAfter(LocalDate.now())) {
                reservationRepository.deleteById(id);
                String emailTemplate = utilMethods.loadEmailTemplateFromResource("deleteReservationEmail.html");
                emailTemplate = emailTemplate.replace("${email}", username);
                emailSender.send(username, emailTemplate, "Reservation was deleted");
            } else {
                throw new CreateReservationException("Reservation can not be deleted");
            }
    }

    @Override
    public void deleteReservationsForUser(Long id) {
        User user = clientRepository.findById(id).orElse(null);
        if(user == null){
            throw new EntityNotFoundException("User does not exist");
        }
        reservationRepository.deleteAllByUser_Id(id);
    }

    @Override
    public List<ReservationResponse> getReservationsByCourt(String court) {
        Court courtEnum = Court.valueOf(court);
        return reservationRepository.findByCourt(courtEnum).stream()
                    .map(reservationMapper::convertToDto).collect(Collectors.toList());
    }

}
