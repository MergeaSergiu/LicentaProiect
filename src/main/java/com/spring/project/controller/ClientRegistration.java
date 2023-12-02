package com.spring.project.controller;


import com.spring.project.dto.AuthenticationRequest;
import com.spring.project.dto.LoginResponse;
import com.spring.project.dto.PasswordResetRequest;
import com.spring.project.dto.RegistrationRequest;
import com.spring.project.model.Client;
import com.spring.project.repository.ClientRepository;
import com.spring.project.service.PasswordResetTokenService;
import com.spring.project.service.RegistrationService;
import com.spring.project.token.PasswordResetToken;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path ="project/auth")
@AllArgsConstructor
@CrossOrigin("*")
public class ClientRegistration {
    private final RegistrationService registrationService;
    private final ClientRepository clientRepository;
    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.register(request));
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {

        return registrationService.confirmToken(token);
    }

    @PostMapping("/authenticate")
    public LoginResponse authenticate(@RequestBody AuthenticationRequest request){
        Client client = clientRepository.findByEmail(request.getEmail()).orElse(null);
        if(client != null && client.getEnabled() && registrationService.checkPasswordsMatch(request.getPassword(),client.getPassword())){
                return registrationService.authenticate(request);
        }else{
            return null;
        }
    }

    @PostMapping("/resetPass")
    public String sendResetPasswordEmail(@RequestBody PasswordResetRequest passwordResetRequest){
        boolean foundClient = clientRepository.findByEmail(passwordResetRequest.getEmail()).isPresent();
        if(foundClient){
            return registrationService.sendResetPasswordEmail(passwordResetRequest);
        }else{
            return null;
        }
    }

    @GetMapping("/confirmResetToken")
    public String confirmResetPassword(@RequestParam("resetToken") String resetToken){
        return registrationService.confirmPasswordResetToken(resetToken);
    }

    @PostMapping("/changePassword")
    public String resetPassword(@RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("resetToken") String resetToken){

        PasswordResetToken passwordResetToken = passwordResetTokenService.getToken(resetToken).orElse(null);
        Client client = passwordResetToken.getClient();
        if(passwordResetToken.getConfirmedAt() != null && client != null && passwordResetToken.getExpiredAt().compareTo(LocalDateTime.now()) > 0 ){
            registrationService.updateClientPassword(client, passwordResetRequest.getNewPassword());
            return "Password update succesfully";
        }else{
            return "First you need to activate your link";
        }

    }

}

