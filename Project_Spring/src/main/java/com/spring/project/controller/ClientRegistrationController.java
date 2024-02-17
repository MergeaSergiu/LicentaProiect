package com.spring.project.controller;


import com.spring.project.Exception.ConfirmAccountException;
import com.spring.project.Exception.EmailNotAvailableException;
import com.spring.project.Exception.ResetPasswordException;
import com.spring.project.dto.AuthenticationResponse;
import com.spring.project.dto.*;
import com.spring.project.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path ="project/auth")
@AllArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class ClientRegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> registration(@Valid @RequestBody RegistrationRequest request) {
        RegistrationResponse registrationResponse = registrationService.register(request);
        return ResponseEntity.ok(registrationResponse);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token, Model model) {
        String message;
        try {
            message = registrationService.confirmToken(token);
            model.addAttribute("message", message);
        } catch (ConfirmAccountException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "confirmAccountResponse.html";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request){
            AuthenticationResponse authenticationResponse = registrationService.authenticate(request);
            return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request)  {
        return ResponseEntity.ok(registrationService.refreshToken(request));
    }

    @PostMapping("/resetPass")
    public ResponseEntity<SendResetPassEmailResponse> sendResetPasswordEmail(@RequestBody SendResetPassEmailRequest resetRequest){
            SendResetPassEmailResponse sendResetPassEmailResponse =  registrationService.sendResetPasswordEmail(resetRequest);
            return ResponseEntity.ok(sendResetPassEmailResponse);
    }

    @GetMapping("/confirmResetToken")
    public String confirmResetPassword(@RequestParam("resetToken") String resetToken, Model model){
        String message;
        try {
            message = registrationService.confirmPasswordResetToken(resetToken);
            model.addAttribute("message", message);
        } catch (ResetPasswordException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "resetPasswordResponse.html";
    }

    @PostMapping("/changePassword")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest){
        PasswordResetResponse passwordResetResponse = registrationService.updateClientPassword(passwordResetRequest);
        return ResponseEntity.ok(passwordResetResponse);
    }
}

