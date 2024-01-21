package com.spring.project.service;

import com.spring.project.token.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenService {

    void savePasswordResetToken(PasswordResetToken passwordResetToken);

    void setConfirmedAt(String passwordResetToken);

    Optional<PasswordResetToken> getToken(String resetToken);
}
