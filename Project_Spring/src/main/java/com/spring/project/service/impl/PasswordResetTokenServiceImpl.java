package com.spring.project.service.impl;


import com.spring.project.repository.PasswordResetTokenRepository;
import com.spring.project.service.PasswordResetTokenService;
import com.spring.project.token.PasswordResetToken;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private PasswordResetTokenRepository passwordResetTokenRepository;

    public void savePasswordResetToken(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public void setConfirmedAt(String passwordResetToken) {
        passwordResetTokenRepository.updateConfirmedAt(
                passwordResetToken, LocalDateTime.now());
    }

    @Override
    public Optional<PasswordResetToken> getToken(String resetToken) {
        return passwordResetTokenRepository.findByToken(resetToken);
    }

    @Override
    public void deleteByclient_Id(Integer id) {
        passwordResetTokenRepository.deleteByclient_Id(id);
    }
}
