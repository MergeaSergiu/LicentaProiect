package com.spring.project.service;

import com.spring.project.controller.ScheduleEmailSender;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailSendingService {

    @Autowired
    private ScheduleEmailSender scheduleEmailSender;

    public void triggerEmailSending(){
        scheduleEmailSender.sendScheduleEmails();
    }
}
