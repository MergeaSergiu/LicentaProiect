package com.spring.project.service.impl;

import com.spring.project.Exception.*;
import com.spring.project.dto.*;
import com.spring.project.email.EmailSender;
import com.spring.project.mapper.AuthenticationMapper;
import com.spring.project.mapper.PasswordResetMapper;
import com.spring.project.mapper.UserMapper;
import com.spring.project.model.Role;
import com.spring.project.model.User;
import com.spring.project.repository.UserRepository;
import com.spring.project.repository.RoleRepository;
import com.spring.project.service.PasswordResetTokenService;
import com.spring.project.service.RegistrationService;
import com.spring.project.token.ConfirmationToken;
import com.spring.project.token.PasswordResetToken;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClientService clientService;
    private final ConfirmationTokenServiceImpl confirmationTokenServiceImpl;
    private final PasswordResetTokenServiceImpl passwordResetTokenServiceImpl;
    private final EmailSender emailSender;
    private final PasswordResetTokenService passwordResetTokenService;
    private final AuthenticationMapper authenticationMapper;
    private final PasswordResetMapper passwordResetMapper;
    private final UserMapper userMapper;
    private final UtilMethods utilMethods;


    public RegistrationResponse register(RegistrationRequest request){
        Role role = roleRepository.findByName("USER");
        if(role == null) {
            throw new EntityNotFoundException("Can not create an user account");
        }
        User user = userMapper.convertFromDto(request,role);
        String receivedToken = clientService.signUpClient(user);

        String link = "https://sport-center-cc69b9715563.herokuapp.com/project/api/v1/auth/confirm?token=" + receivedToken;
        String emailTemplate = utilMethods.loadEmailTemplateFromResource("confirmAccountEmail.html");
        emailTemplate = emailTemplate.replace("${user}", user.getFirstName()+" " + user.getLastName());
        emailTemplate = emailTemplate.replace("${resetLink}",link);

        emailSender.send(request.getEmail(), emailTemplate,"Activate your account");
        return RegistrationResponse
                .builder()
                .registrationResponse("Account was created successfully. You need to activate it.")
                .build();
    }

    @Override
    public SendResetPassEmailResponse sendResetPasswordEmail(SendResetPassEmailRequest resetPassEmailRequest){
        String requestClientEmail = resetPassEmailRequest.getEmail();
        boolean isValidEmail = emailValidator.test(requestClientEmail);
        if (!isValidEmail) {
            throw new IllegalArgumentException("Email does not respect the criteria");
        }
        User user = userRepository.findByEmail(resetPassEmailRequest.getEmail()).orElseThrow(() -> new EntityNotFoundException("There is no account with this email"));
        String resetToken = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(
                resetToken,
                user
        );
        passwordResetTokenServiceImpl.savePasswordResetToken(passwordResetToken);
        String link = "https://sport-center-cc69b9715563.herokuapp.com/project/api/v1/auth/confirmResetToken?resetToken=" + resetToken;
        String emailTemplate = utilMethods.loadEmailTemplateFromResource("resetPasswordEmail.html");
        emailTemplate = emailTemplate.replace("${user}", user.getFirstName()+" " + user.getLastName());
        emailTemplate = emailTemplate.replace("${resetLink}",link);
        emailSender.send(requestClientEmail, emailTemplate, "Reset your password");
        return SendResetPassEmailResponse.builder()
                .token(resetToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
        if(!user.getEnabled()){
            throw new EntityNotFoundException("Account is not confirmed");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        String jwt = jwtService.generateToken(user.getEmail(), user.getRole().getName());
        String refreshJwt = jwtService.generateRefreshToken(user.getEmail(), user.getRole().getName());
        String userRole = jwtService.extractClientRole(jwt);
        return authenticationMapper.convertToDto(jwt,refreshJwt, userRole);
    }

    @Transactional
    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenServiceImpl.getToken(token).orElse(null);
        if(confirmationToken == null){
            throw new ConfirmAccountException("Confirmation email is not available anymore. Request another one.");
        }
        else if(confirmationToken.getConfirmedAt() != null){
            throw new ConfirmAccountException("Your account was already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new ConfirmAccountException("The request has expired. Please create a new account");
        }
        confirmationTokenServiceImpl.setConfirmedAt(token);
        userRepository.enableClient(confirmationToken.getUser().getEmail());
        return "Your account is now confirmed. Please log in";
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
        User user = passwordResetToken.getUser();

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
        if(user != null && passwordResetToken.getExpiredAt().isAfter(LocalDateTime.now())) {
            passwordResetToken.setAlreadyUsed(true);
            clientService.resetClientPassword(user, passwordResetRequest.getNewPassword());
            return passwordResetMapper.convertToDto("Password was updated. Please log in");
        }else {
            throw new ResetPasswordException("Password could not be updated");
        }
    }

    public AuthenticationResponse refreshToken(JwtRefreshToken jwtRefreshToken) {
        String refreshJwt = jwtRefreshToken.getRefreshToken();
        String clientEmail = jwtService.extractClientUsername(refreshJwt);
        if(clientEmail == null){
            throw new EntityNotFoundException("Token could not be updated");
        }
        var clientDetails = this.userRepository.findByEmail(clientEmail).orElseThrow();
        if(jwtService.isTokenValid(refreshJwt, clientDetails)){
            var accessToken = jwtService.generateToken(clientDetails.getEmail(), clientDetails.getRole().getName());
            String userRole = jwtService.extractClientRole(accessToken);
            return authenticationMapper.convertToDto(accessToken, refreshJwt, userRole);
        }else{
            throw new CustomExpiredTokenException("Session has expired");
        }
    }
}
