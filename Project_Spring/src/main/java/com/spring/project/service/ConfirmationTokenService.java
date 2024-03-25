package com.spring.project.service;

import com.spring.project.token.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken confirmationToken);

    Optional<ConfirmationToken> getToken(String token);

    void deleteByclient_Id(Long id);

    void setConfirmedAt(String token);

    ConfirmationToken findTokenByUserId(Long id);
}
