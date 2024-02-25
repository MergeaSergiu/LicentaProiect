package com.spring.project.service;

import com.spring.project.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface RegistrationService {

    RegistrationResponse register(RegistrationRequest registrationRequest);

    SendResetPassEmailResponse sendResetPasswordEmail(SendResetPassEmailRequest resetRequest);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    String confirmToken(String token);

    String confirmPasswordResetToken(String token);

    PasswordResetResponse updateClientPassword(PasswordResetRequest passwordResetRequest);

    AuthenticationResponse refreshToken(JwtRefreshToken jwtRefreshToken);

}
