package com.spring.project.service.impl;

import com.spring.project.Exception.*;
import com.spring.project.dto.*;
import com.spring.project.email.EmailSender;
import com.spring.project.model.Client;
import com.spring.project.model.Role;
import com.spring.project.repository.ClientRepository;
import com.spring.project.repository.RoleRepsitory;
import com.spring.project.service.PasswordResetTokenService;
import com.spring.project.service.RegistrationService;
import com.spring.project.token.ConfirmationToken;
import com.spring.project.token.PasswordResetToken;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;
    private final ClientRepository clientRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClientService clientService;
    private final ConfirmationTokenServiceImpl confirmationTokenServiceImpl;
    private final PasswordResetTokenServiceImpl passwordResetTokenServiceImpl;
    private final EmailSender emailSender;
    private final PasswordResetTokenService passwordResetTokenService;
    private final RoleRepsitory roleRepsitory;

    private String loadEmailTemplateFromResource(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public RegistrationResponse register(RegistrationRequest request){
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new EmailNotAvailableException("Email is not valid");
        }
        boolean isValidPassword = passwordValidator.test(request.getPassword());
        if(!isValidPassword){
            throw new InvalidCredentialsException("Password do not respect the criteria");
        }

        Role role = roleRepsitory.findByName("USER");

        Client client = new Client(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                role
        );
        String receivedToken = clientService.signUpClient(client);

        String link = "http://localhost:8080/project/auth/confirm?token=" + receivedToken;
        String emailTemplate = loadEmailTemplateFromResource("confirmAccountEmail.html");
        emailTemplate = emailTemplate.replace("${email}", request.getEmail());
        emailTemplate = emailTemplate.replace("${resetLink}",link);

        emailSender.send(request.getEmail(), emailTemplate,"activate your account");
        return RegistrationResponse.builder().
                registrationResponse("Account was created successfully. You need to activate it.")
                .build();
    }

    @Override
    public SendResetPassEmailResponse sendResetPasswordEmail(SendResetPassEmailRequest resetPassEmailRequest){
        String requestClientEmail = resetPassEmailRequest.getEmail();
        boolean isValidEmail = emailValidator.test(requestClientEmail);
        if (!isValidEmail) {
            throw new EmailNotAvailableException("Email does not respect the criteria");
        }

        Client client = clientService.findClientByEmail(resetPassEmailRequest.getEmail());
        if(client == null){
            throw new ResetPasswordException("There is no account with this email");
        }

        String resetToken = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(
                resetToken,
                client
        );
        passwordResetTokenServiceImpl.savePasswordResetToken(passwordResetToken);
        String link = "http://localhost:8080/project/auth/confirmResetToken?resetToken=" + resetToken;
        String emailTemplate = loadEmailTemplateFromResource("resetPasswordEmail.html");
        emailTemplate = emailTemplate.replace("${email}", resetPassEmailRequest.getEmail());
        emailTemplate = emailTemplate.replace("${resetLink}",link);
        emailSender.send(requestClientEmail, emailTemplate, "Reset your password");
        return SendResetPassEmailResponse.builder()
                .token(resetToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var client = clientRepository.findByEmail(authenticationRequest.getEmail())
                    .orElseThrow();
        String jwt = jwtService.generateToken(client.getEmail(), client.getRole().getName());
        String refreshJwt = jwtService.generateRefreshToken(client.getEmail(), client.getRole().getName());
        String userRole = jwtService.extractClientRole(jwt);
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshJwt)
                .user_Role(userRole)
                .build();
    }

    @Transactional
    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenServiceImpl.getToken(token).orElse(null);

        if(confirmationToken == null){
            throw new ConfirmAccountException("Confirmation email is not available anymore");
        }
        else if(confirmationToken.getConfirmedAt() != null){
            throw new ConfirmAccountException("Account was already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new ConfirmAccountException("The request expired. Please create a new account");
        }
        confirmationTokenServiceImpl.setConfirmedAt(token);
        clientService.enableClient(
                confirmationToken.getClient().getEmail());
        return "Account was confirmed. Please log in";
    }

    @Transactional
    @Override
    public String confirmPasswordResetToken(String token) {
        PasswordResetToken passwordResetToken =  passwordResetTokenServiceImpl.getToken(token).orElse(null);

        if(passwordResetToken == null){
            throw new ResetPasswordException("Confirmation email is not available anymore");
        }
        else if(passwordResetToken.getConfirmedAt() != null){
                throw  new ResetPasswordException("Verification link was already confirmed");
        }

        LocalDateTime expiredAt = passwordResetToken.getExpiredAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new ResetPasswordException("The request expired. Please create a new account.");
        }
        passwordResetTokenServiceImpl.setConfirmedAt(token);
        return "Your request was confirmed. Please change your password";
    }

    @Override
    public PasswordResetResponse updateClientPassword(PasswordResetRequest passwordResetRequest) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.getToken(passwordResetRequest.getToken()).orElseThrow();
        Client client = passwordResetToken.getClient();

        boolean isValidPassword = passwordValidator.test(passwordResetRequest.getNewPassword());
        if (!isValidPassword) {
            throw new ResetPasswordException("Password must respect the criteria");
        }
        if(passwordResetToken.isAlreadyUsed()){
            throw new ResetPasswordException("The password was already reset with this request.");
        }
        if(passwordResetToken.getConfirmedAt() == null){
            throw new ResetPasswordException("The request is not activated yet.");
        }
        if(!passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmedPassword())){
            throw new ResetPasswordException("Password are not matching. Please write again the passwords");
        }
        if(passwordResetToken.getConfirmedAt() != null && client != null && passwordResetToken.getExpiredAt().compareTo(LocalDateTime.now()) > 0 ) {
            passwordResetToken.setAlreadyUsed(true);
            clientService.resetClientPassword(client, passwordResetRequest.getNewPassword());
            return PasswordResetResponse.builder()
                    .passwordResetResponse("Password was updated. Please log in")
                    .build();
        }else {
            throw new ResetPasswordException("Password could not be updated");
        }
    }

    public AuthenticationResponse refreshToken(JwtRefreshToken jwtRefreshToken) {
        final String clientEmail;
        String refreshJwt = jwtRefreshToken.getRefreshToken();
        clientEmail = jwtService.extractClientUsername(refreshJwt);
        if (clientEmail != null){
            var clientDetails = this.clientRepository.findByEmail(clientEmail).orElseThrow();
            if(jwtService.isTokenValid(refreshJwt, clientDetails)){
                var accessToken = jwtService.generateToken(clientDetails.getEmail(), clientDetails.getRole().getName());
                String userRole = jwtService.extractClientRole(accessToken);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshJwt)
                        .user_Role(userRole)
                        .build();
            }else{
                throw new CustomExpiredJwtException("Refresh Token expired");
            }
        }
        throw new CustomExpiredJwtException("Token could not be updated");
    }
}
