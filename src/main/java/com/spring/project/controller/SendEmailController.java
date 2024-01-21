package com.spring.project.controller;

import com.spring.project.service.impl.EmailSendingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "project/email")
@AllArgsConstructor
public class SendEmailController {

    private final EmailSendingService emailSendingService;

    @GetMapping("/send")
    public String sendEmail(){
        emailSendingService.triggerEmailSending();
        return "Emails sent!!";
    }


}

