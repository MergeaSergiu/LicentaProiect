package com.spring.project.service.impl;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.ReservationRequest;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.email.EmailSender;
import com.spring.project.model.CourtReservation;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final FotballReservationServiceImpl fotballReservationServiceImpl;
    private final ReservationRepository reservationRepository;
    private final EmailSender emailSender;

    public void saveReservation(ReservationRequest reservationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CourtReservation courtReservation = new CourtReservation(
                reservationRequest.getLocalDate(),
                reservationRequest.getHourSchedule(),
                reservationRequest.getCourt(),
                authentication.getName()
        );
        fotballReservationServiceImpl.save(courtReservation);
        String emailTemplate = loadEmailTemplateFromResource("reservationResponseEmail.html");
        emailTemplate = emailTemplate.replace("${email}", authentication.getName());
        emailTemplate = emailTemplate.replace("${hourSchedule}", reservationRequest.getHourSchedule());
        emailTemplate = emailTemplate.replace("${dateTime}",reservationRequest.getLocalDate().toString());
        emailSender.send(authentication.getName(), emailTemplate, "Thank you for your reservation");

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


    public void sendEmails() {
        List<CourtReservation> reservations = fotballReservationServiceImpl.getReservationWithCurrentDay();
        for(String email : reservations.stream()
                .map(CourtReservation::getEmail)
                .distinct()
                .collect(Collectors.toList())
        )
        {
            String emailTemplate = loadEmailTemplateFromResource("remainderEmail.html");
            emailTemplate = emailTemplate.replace("${email}", email);
            emailSender.send(email, emailTemplate, "RemainderEmail");
        }
    }

    @Override
    public List<CourtReservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<CourtReservation> getAllClientReservation(String clientEmail) {
        return reservationRepository.findReservationsByUser(clientEmail);
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
    public void deleteReservationByUserEmail(String email) {
        reservationRepository.deleteByEmail(email);
    }

    @Override
    public List<ReservationResponse> getReservationsByCourt(String court) {
            List<CourtReservation> courtReservations = fotballReservationServiceImpl.getReservationsByCourt(court);
            if(courtReservations != null) {
                return courtReservations.stream()
                        .map(courtReservation -> ReservationResponse.builder()
                                .id(courtReservation.getId())
                                .localDate(courtReservation.getLocalDate().toString())
                                .hourSchedule(courtReservation.getHourSchedule())
                                .clientEmail(courtReservation.getEmail())
                                .court(null)
                                .build())
                        .collect(Collectors.toList());
            }else {
                return new ArrayList<>();
            }
    }

}
