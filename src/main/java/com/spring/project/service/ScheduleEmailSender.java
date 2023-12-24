package com.spring.project.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduleEmailSender {

    private final ReservationService reservationService;

    public void sendScheduleEmails(){
         reservationService.sendEmails();
    }

}
