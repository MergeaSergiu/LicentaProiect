package com.spring.project.service;

import com.spring.project.dto.*;

public interface RegistrationService {

    RegistrationResponse register(RegistrationRequest registrationRequest);

    SendResetPassEmailResponse sendResetPasswordEmail(SendResetPassEmailRequest resetRequest);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    String confirmToken(String token);

    String confirmPasswordResetToken(String token);

    PasswordResetResponse updateClientPassword(PasswordResetRequest passwordResetRequest);

    AuthenticationResponse refreshToken(JwtRefreshToken jwtRefreshToken);

}
