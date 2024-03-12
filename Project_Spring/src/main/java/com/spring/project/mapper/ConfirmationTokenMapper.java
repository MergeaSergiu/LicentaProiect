package com.spring.project.mapper;

import com.spring.project.model.Client;
import com.spring.project.token.ConfirmationToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ConfirmationTokenMapper {

    public ConfirmationToken createConfirmationToken(String confirmationToken, LocalDateTime localDateTime, Client user){
        return ConfirmationToken
                .builder()
                .token(confirmationToken)
                .createdAt(localDateTime)
                .expiredAt(localDateTime.plusMinutes(60))
                .client(user)
                .build();
    }
}
