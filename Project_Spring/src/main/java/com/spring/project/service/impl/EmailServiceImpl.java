package com.spring.project.service.impl;

import com.spring.project.email.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailSender {

    private final static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender javaMailSender;

    @Override
    @Async
    public void send(String to, String email, String subject) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setText(email, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("sportsArena@gmail.com");
            javaMailSender.send(mimeMessage);
        }catch(MessagingException e){
            logger.error("failed to send email", e);
            throw new IllegalStateException("failed to sent email");
        }
    }
}
