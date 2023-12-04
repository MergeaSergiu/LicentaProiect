package com.spring.project.controller;


import com.spring.project.Exception.EmailNotAvailableException;
import com.spring.project.Exception.InvalidCredentialsException;
import com.spring.project.dto.AuthenticationResponse;
import com.spring.project.dto.*;
import com.spring.project.model.Client;
import com.spring.project.repository.ClientRepository;
import com.spring.project.service.PasswordResetTokenService;
import com.spring.project.service.RegistrationService;
import com.spring.project.token.PasswordResetToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path ="project/auth")
@AllArgsConstructor
@Validated
public class ClientRegistration {
    private final RegistrationService registrationService;
    private final ClientRepository clientRepository;
    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.register(request));
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        Client client = clientRepository.findByEmail(request.getEmail()).orElse(null);
        if(client != null && client.getEnabled() && registrationService.checkPasswordsMatch(request.getPassword(),client.getPassword())){
                return ResponseEntity.ok(registrationService.authenticate(request));
        }else{
            throw new InvalidCredentialsException("Invalid email or passoword");
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        registrationService.refreshToken(request,response);

    }

    @PostMapping("/resetPass")
    public String sendResetPasswordEmail(@RequestBody PasswordResetRequest passwordResetRequest){
        boolean foundClient = clientRepository.findByEmail(passwordResetRequest.getEmail()).isPresent();
        if(foundClient){
            return registrationService.sendResetPasswordEmail(passwordResetRequest);
        }else{
            throw new InvalidCredentialsException("Username does not exist");
        }
    }

    @GetMapping("/confirmResetToken")
    public String confirmResetPassword(@RequestParam("resetToken") String resetToken){
        return registrationService.confirmPasswordResetToken(resetToken);
    }

    @PostMapping("/changePassword")
    public String resetPassword(@RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("resetToken") String resetToken){

        if(passwordResetRequest.getNewPassword().isEmpty() || passwordResetRequest.getNewPassword().isBlank()){
            throw new InvalidCredentialsException("Password field is emplty. Please choose a password");
        }
        PasswordResetToken passwordResetToken = passwordResetTokenService.getToken(resetToken).orElseThrow();
        if(passwordResetToken.isAlreadyUsed()){
            throw new EmailNotAvailableException("Change Password request was already used");
        }else{
            passwordResetToken.setAlreadyUsed(true);
        }
        Client client = passwordResetToken.getClient();
        if(passwordResetToken.getConfirmedAt() != null && client != null && passwordResetToken.getExpiredAt().compareTo(LocalDateTime.now()) > 0 ){
            registrationService.updateClientPassword(client, passwordResetRequest.getNewPassword());
            return "Password updated succesfully";
        }else{
            throw new EmailNotAvailableException("Activate your request first");
        }
    }

    @GetMapping("/demo")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Ai accesat secured endpoint");
    }
}

