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
import com.spring.project.repository.ClientRepository;
import com.spring.project.repository.CourtDetailsRepository;
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
    private final CourtDetailsRepository courtDetailsRepository;

    public void saveReservation(ReservationRequest reservationRequest, String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = clientRepository.findByEmail(username).orElse(null);
            if (user == null) {
                throw new EntityNotFoundException("User does not exist");
            }

            Court court = Court.valueOf(reservationRequest.getCourt());
            CourtDetails courtDetails = courtDetailsRepository.findByCourt(court);
            if(courtDetails == null) throw new EntityNotFoundException("Court does not exist");

            String[] parts = reservationRequest.getHourSchedule().split("-");
            Integer startTime = Integer.parseInt(parts[0]);
            Integer endTime = Integer.parseInt(parts[1]);

            if(startTime.compareTo(courtDetails.getStartTime()) <0 || endTime.compareTo(courtDetails.getEndTime()) >0){
                throw new EntityNotFoundException("Time slots are outside the court time slots");
            }

            if(startTime.compareTo(endTime) >=0){
            throw new EntityNotFoundException("Start time is before the end Time");
            }
            if(endTime - startTime > 1){
            throw new EntityNotFoundException("You can not make reservation for more than 1 hour");
            }

            boolean existingReservation = reservationRepository.findAll().stream()
                    .anyMatch(reservation -> reservation.getReservationDate().toString().equals(reservationRequest.getLocalDate())
                            && (reservation.getStartTime().compareTo(startTime)) == 0
                            && reservation.getCourt().equals(court));

            if(existingReservation){
                throw new CreateReservationException("There is a reservation at the same moment created");
            }
            List<Reservation> reservationsForCurrentDayForUser = reservationRepository.findAllByUser_IdAndReservationMadeDate(user.getId(), LocalDate.now());
            if(reservationsForCurrentDayForUser.size() >=3){
                throw new CreateReservationException("You reached the reservations limit per day");
            }
            Reservation reservation = reservationMapper.convertFromDto(reservationRequest, user, startTime, endTime);
            reservationRepository.save(reservation);
            String emailTemplate = utilMethods.loadEmailTemplateFromResource("reservationResponseEmail.html");
            emailTemplate = emailTemplate.replace("${user}", user.getFirstName()+" " + user.getLastName());
            emailTemplate = emailTemplate.replace("${hourSchedule}", reservationRequest.getHourSchedule());
            emailTemplate = emailTemplate.replace("${court}", reservationRequest.getCourt());
            emailTemplate = emailTemplate.replace("${dateTime}", reservationRequest.getLocalDate());
            emailSender.send(username, emailTemplate, "Thank you for your reservation");
    }

    @Override
    public void saveReservationByAdmin(ReservationRequestByAdmin reservationRequestByAdmin) {
        User user = clientRepository.findById(reservationRequestByAdmin.getUserId()).orElse(null);
        if(user == null){
            throw new EntityNotFoundException("User does not exist");
        }

        Court court = Court.valueOf(reservationRequestByAdmin.getCourt());
        CourtDetails courtDetails = courtDetailsRepository.findByCourt(court);
        if(courtDetails == null) throw new EntityNotFoundException("Court does not exist");

        String[] parts = reservationRequestByAdmin.getHourSchedule().split("-");
        Integer startTime = Integer.parseInt(parts[0]);
        Integer endTime = Integer.parseInt(parts[1]);

        if(startTime.compareTo(courtDetails.getStartTime()) <0 || endTime.compareTo(courtDetails.getEndTime()) >0){
            throw new EntityNotFoundException("Choose other timeSlots");
        }

        if(startTime.compareTo(endTime) >=0){
            throw new EntityNotFoundException("Start time is before the end Time");
        }
        if(endTime - startTime > 1){
            throw new EntityNotFoundException("You can not make reservation for more than 1 hour");
        }

        boolean existingReservation = reservationRepository.findAll().stream()
                .anyMatch(reservation -> reservation.getReservationDate().toString().equals(reservationRequestByAdmin.getLocalDate())
                        && (reservation.getStartTime().compareTo(Integer.parseInt(reservationRequestByAdmin.getHourSchedule().split("-")[0])) == 0)
                        && reservation.getCourt().equals(Court.valueOf(reservationRequestByAdmin.getCourt())));
        if(existingReservation){
            throw new CreateReservationException("There is a reservation at the same moment created");
        }

        List<Reservation> reservationsForCurrentDayForUser = reservationRepository.findAllByUser_IdAndReservationMadeDate(user.getId(), LocalDate.now());
        if(reservationsForCurrentDayForUser.size() >=3){
            throw new CreateReservationException("User reached the limit of 3 reservations per day");
        }

        Reservation reservation = reservationMapper.convertDtoAdminReservation(reservationRequestByAdmin, user,startTime,endTime);
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
            if(reservation.getReservationDate().isBefore(LocalDate.now())){
                throw new CreateReservationException("Reservation is in the past.");
            }
            reservationRepository.deleteById(id);
            String emailTemplate = utilMethods.loadEmailTemplateFromResource("deleteReservationEmail.html");
            emailTemplate = emailTemplate.replace("${user}", user.getFirstName()+" " + user.getLastName());
            emailSender.send(username, emailTemplate, "Reservation was deleted");
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
