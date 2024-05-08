package com.spring.project.service.impl;

import com.spring.project.Exception.CreateReservationException;
import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationRequestByAdmin;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.email.EmailSender;
import com.spring.project.mapper.ReservationMapper;
import com.spring.project.model.Court;
import com.spring.project.model.CourtDetails;
import com.spring.project.model.User;
import com.spring.project.model.Reservation;
import com.spring.project.repository.UserRepository;
import com.spring.project.repository.CourtDetailsRepository;
import com.spring.project.repository.ReservationRepository;
import com.spring.project.service.ReservationService;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final EmailSender emailSender;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;
    private final UtilMethods utilMethods;
    private final CourtDetailsRepository courtDetailsRepository;

    public void saveReservation(ReservationRequest reservationRequest, String authorization) {
        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(reservationRequest.getLocalDate(), formatter).isBefore(LocalDate.now())) {
            throw new CreateReservationException("Can not create reservations in past");
        }
        Court court = Court.valueOf(reservationRequest.getCourt());
        CourtDetails courtDetails = courtDetailsRepository.findByCourt(court);
        if (courtDetails == null) throw new EntityNotFoundException("Court does not exist");

        String[] parts = reservationRequest.getHourSchedule().split("-");
        Integer startTime = Integer.parseInt(parts[0]);
        Integer endTime = Integer.parseInt(parts[1]);

        if (startTime.compareTo(courtDetails.getStartTime()) < 0 || endTime.compareTo(courtDetails.getEndTime()) > 0) {
            throw new CreateReservationException("Time slots are outside the court time slots");
        }

        if (startTime.compareTo(endTime) >= 0) {
            throw new CreateReservationException("Start time is before the end Time");
        }
        if (endTime - startTime > 1) {
            throw new CreateReservationException("You can not make reservation for more than 1 hour");
        }

        boolean existingReservation = reservationRepository.findAll().stream().anyMatch(reservation -> reservation.getReservationDate().toString().equals(reservationRequest.getLocalDate()) && (reservation.getStartTime().compareTo(startTime)) == 0 && reservation.getCourt().equals(court));

        if (existingReservation) {
            throw new CreateReservationException("There is a reservation at the same moment created");
        }
        List<Reservation> reservationsForCurrentDayForUser = reservationRepository.findAllByUser_IdAndReservationMadeDate(user.getId(), LocalDate.now());
        if (reservationsForCurrentDayForUser.size() >= 3) {
            throw new CreateReservationException("You reached the reservations limit per day");
        }
        Reservation reservation = reservationMapper.convertFromDto(reservationRequest, user, startTime, endTime);
        reservationRepository.save(reservation);
        String emailTemplate = utilMethods.loadEmailTemplateFromResource("reservationResponseEmail.html");
        emailTemplate = emailTemplate.replace("${user}", user.getFirstName() + " " + user.getLastName());
        emailTemplate = emailTemplate.replace("${hourSchedule}", reservationRequest.getHourSchedule());
        emailTemplate = emailTemplate.replace("${court}", reservationRequest.getCourt());
        emailTemplate = emailTemplate.replace("${dateTime}", reservationRequest.getLocalDate());
        emailSender.send(user.getEmail(), emailTemplate, "Thank you for your reservation");
    }

    @Override
    public void saveReservationByAdmin(ReservationRequestByAdmin reservationRequestByAdmin) {
        User user = userRepository.findById(reservationRequestByAdmin.getUserId()).orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        if (!user.getEnabled()) {
            throw new EntityNotFoundException("This account is not enabled");
        }

        if (user.getRole().getName().equals("TRAINER")) {
            throw new CreateReservationException("Can not create a reservation for a Trainer");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(reservationRequestByAdmin.getLocalDate(), formatter).isBefore(LocalDate.now())) {
            throw new CreateReservationException("Can not create reservations in past");
        }

        Court court = Court.valueOf(reservationRequestByAdmin.getCourt());
        CourtDetails courtDetails = courtDetailsRepository.findByCourt(court);
        if (courtDetails == null) throw new EntityNotFoundException("Court does not exist");

        String[] parts = reservationRequestByAdmin.getHourSchedule().split("-");
        Integer startTime = Integer.parseInt(parts[0]);
        Integer endTime = Integer.parseInt(parts[1]);

        if (startTime.compareTo(courtDetails.getStartTime()) < 0 || endTime.compareTo(courtDetails.getEndTime()) > 0) {
            throw new EntityNotFoundException("Choose other timeSlots");
        }

        if (startTime.compareTo(endTime) >= 0) {
            throw new EntityNotFoundException("Start time is before the end Time");
        }
        if (endTime - startTime > 1) {
            throw new EntityNotFoundException("You can not make reservation for more than 1 hour");
        }

        boolean existingReservation = reservationRepository.findAll().stream().anyMatch(reservation -> reservation.getReservationDate().toString().equals(reservationRequestByAdmin.getLocalDate()) && (reservation.getStartTime().compareTo(Integer.parseInt(reservationRequestByAdmin.getHourSchedule().split("-")[0])) == 0) && reservation.getCourt().equals(Court.valueOf(reservationRequestByAdmin.getCourt())));
        if (existingReservation) {
            throw new CreateReservationException("There is a reservation at the same moment created");
        }

        List<Reservation> reservationsForCurrentDayForUser = reservationRepository.findAllByUser_IdAndReservationMadeDate(user.getId(), LocalDate.now());
        if (reservationsForCurrentDayForUser.size() >= 3) {
            throw new CreateReservationException("User reached the limit of 3 reservations per day");
        }

        Reservation reservation = reservationMapper.convertDtoAdminReservation(reservationRequestByAdmin, user, startTime, endTime);
        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationResponse> getAllReservations() {

        return reservationRepository.findAllByOrderByReservationDateAsc().stream().map(reservationMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> getAllUserReservations(String authorization) {

        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        return reservationRepository.findAllByUser_IdOrderByReservationDateAsc(user.getId()).stream().map(reservationMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteReservation(Long id, String authorization) {

        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new CreateReservationException("Reservation does not exist"));
        if (reservation.getUser() == null) {
            throw new EntityNotFoundException("There is no user associated with this reservation");
        }

        if (user.getRole().toString().equals("USER")) {
            int compare = Long.compare(reservation.getUser().getId(), user.getId());

            if (compare != 0) {
                throw new EntityNotFoundException("User tried to delete other user reservation");
            }
        }
        if (reservation.getReservationDate().isBefore(LocalDate.now())) {
            throw new CreateReservationException("Reservation is in the past.");
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public void deleteReservationsForUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
        reservationRepository.deleteAllByUser_Id(user.getId());
    }

    @Override
    public List<ReservationResponse> getReservationsByCourt(String court) {
        Court courtEnum = Court.valueOf(court);
        CourtDetails courtDetails = courtDetailsRepository.findByCourt(courtEnum);
        if (courtDetails == null) throw new EntityNotFoundException("Court does not exist");
        return reservationRepository.findByCourt(courtEnum).stream().map(reservationMapper::convertToDto).collect(Collectors.toList());
    }

}
