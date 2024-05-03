package com.spring.project.controller;


import com.spring.project.Exception.ConfirmAccountException;
import com.spring.project.Exception.ResetPasswordException;
import com.spring.project.dto.AuthenticationResponse;
import com.spring.project.dto.*;
import com.spring.project.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path ="project/api/v1/auth")
@AllArgsConstructor
@Validated
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> registration(@RequestBody RegistrationRequest request) {
        RegistrationResponse registrationResponse = registrationService.register(request);
        return new ResponseEntity<>(registrationResponse, HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token, Model model) {
        String message;
        try {
            message = registrationService.confirmToken(token);
            model.addAttribute("message", message);
        } catch (ConfirmAccountException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "confirmAccountResponse.html";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request){
            AuthenticationResponse authenticationResponse = registrationService.authenticate(request);
            return new ResponseEntity<>(authenticationResponse,HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody JwtRefreshToken jwtRefreshToken)  {
        AuthenticationResponse authenticationResponse = registrationService.refreshToken(jwtRefreshToken);
        return new ResponseEntity<>(authenticationResponse,HttpStatus.OK);
    }

    @PostMapping("/resetPass")
    public ResponseEntity<SendResetPassEmailResponse> sendResetPasswordEmail(@RequestBody SendResetPassEmailRequest resetRequest){
            SendResetPassEmailResponse sendResetPassEmailResponse =  registrationService.sendResetPasswordEmail(resetRequest);
            return new ResponseEntity<>(sendResetPassEmailResponse,HttpStatus.OK);
    }

    @GetMapping("/confirmResetToken")
    public String confirmResetPassword(@RequestParam("resetToken") String resetToken, Model model){
        String message;
        try {
            message = registrationService.confirmPasswordResetToken(resetToken);
            model.addAttribute("message", message);
        } catch (ResetPasswordException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "resetPasswordResponse.html";
    }

    @PostMapping("/changePassword")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest){
        PasswordResetResponse passwordResetResponse = registrationService.updateClientPassword(passwordResetRequest);
        return new ResponseEntity<>(passwordResetResponse,HttpStatus.OK);
    }
}
