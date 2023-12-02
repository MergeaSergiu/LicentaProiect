package com.spring.project.service;


import com.spring.project.repository.PasswordResetTokenRepository;
import com.spring.project.token.PasswordResetToken;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PasswordResetTokenService {

    private PasswordResetTokenRepository passwordResetTokenRepository;

    public void savePasswordResetToken(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public void setConfirmedAt(String passwordResetToken) {
        passwordResetTokenRepository.updateConfirmedAt(
                passwordResetToken, LocalDateTime.now());
    }

    public Optional<PasswordResetToken> getToken(String resetToken) {
        return passwordResetTokenRepository.findByToken(resetToken);
    }
}
