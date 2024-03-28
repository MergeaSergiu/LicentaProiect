package com.spring.project.mapper;

import com.spring.project.model.User;
import com.spring.project.token.ConfirmationToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ConfirmationTokenMapper {

    public ConfirmationToken createConfirmationToken(String confirmationToken, LocalDateTime localDateTime, User user){
        return ConfirmationToken
                .builder()
                .token(confirmationToken)
                .createdAt(localDateTime)
                .expiredAt(localDateTime.plusMinutes(60))
                .user(user)
                .build();
    }
}
