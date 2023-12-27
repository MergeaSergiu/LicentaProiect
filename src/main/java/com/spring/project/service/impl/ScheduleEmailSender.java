package com.spring.project.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduleEmailSender {

    private final ReservationServiceImpl reservationServiceImpl;

    public void sendScheduleEmails(){
         reservationServiceImpl.sendEmails();
    }

}
