package com.spring.project.service;

import com.spring.project.dto.AuthenticationRequest;
import com.spring.project.dto.AuthenticationResponse;
import com.spring.project.dto.PasswordResetRequest;
import com.spring.project.dto.RegistrationRequest;
import com.spring.project.model.Client;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface RegistrationService {

    String register(RegistrationRequest registrationRequest);

    String sendResetPasswordEmail(PasswordResetRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    String confirmToken(String token);

    String confirmPasswordResetToken(String token);

    void updateClientPassword(Client client, String newPassword);

    void refreshToken(HttpServletRequest request,
                      HttpServletResponse response) throws IOException;

}
