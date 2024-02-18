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
        String hourSchedule = reservationRequest.getStartTime()+ "-" + reservationRequest.getEndTime();
        CourtReservation courtReservation = new CourtReservation(
                reservationRequest.getLocalDate(),
                hourSchedule,
                reservationRequest.getCourt(),
                authentication.getName()
        );
        String emailTemplate = loadEmailTemplateFromResource("reservationResponseEmail.html");
        emailTemplate = emailTemplate.replace("${email}", authentication.getName());
        emailTemplate = emailTemplate.replace("${hourSchedule}", reservationRequest.getStartTime());
        emailTemplate = emailTemplate.replace("${dateTime}",reservationRequest.getLocalDate().toString());
        emailSender.send(authentication.getName(), emailTemplate, "Thank you for your reservation");
        fotballReservationServiceImpl.save(courtReservation);
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
    public List<CourtReservation> getAllClientReservation(String clientEmail) {
        return reservationRepository.findReservationsByUser(clientEmail);
    }

    @Override
    public void deleteReservation(String startTime, String endTime, LocalDate localDate, String court) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String hourSchedule = startTime + "-" + endTime;
        fotballReservationServiceImpl.deleteReservation(authentication.getName(),hourSchedule, localDate, court);
        String emailTemplate = loadEmailTemplateFromResource("deleteReservationEmail.html");
        emailTemplate = emailTemplate.replace("${email}", authentication.getName());
        emailSender.send(authentication.getName(), emailTemplate, "Reservation was deleted");
    }

    @Override
    public List<ReservationResponse> getReservationsByCourt(String court) {
            List<CourtReservation> courtReservations = fotballReservationServiceImpl.getReservationsByCourt(court);
            if(courtReservations != null) {
                return courtReservations.stream()
                        .map(courtReservation -> ReservationResponse.builder()
                                .localDate(courtReservation.getLocalDate())
                                .startTime(courtReservation.getHourSchedule().split("-")[0])
                                .endTime(courtReservation.getHourSchedule().split("-")[1])
                                .clientEmail(courtReservation.getEmail())
                                .court(null)
                                .build())
                        .collect(Collectors.toList());
            }else {
                return new ArrayList<>();
            }
    }

}
