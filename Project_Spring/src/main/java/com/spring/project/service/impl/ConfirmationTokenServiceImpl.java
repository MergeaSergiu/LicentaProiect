package com.spring.project.service.impl;

import com.spring.project.repository.ConfirmationTokenRepository;
import com.spring.project.service.ConfirmationTokenService;
import com.spring.project.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(ConfirmationToken confirmationToken){
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public void deleteByclient_Id(Integer id) {
        confirmationTokenRepository.deleteByclient_Id(id);
    }

    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public ConfirmationToken findTokenByUserId(int id){
        return confirmationTokenRepository.findByclient_Id(id);
    }
}
