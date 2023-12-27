package com.spring.project.service.impl;

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
