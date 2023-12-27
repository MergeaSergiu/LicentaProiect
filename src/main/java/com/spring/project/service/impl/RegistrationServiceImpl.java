package com.spring.project.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.project.Exception.EmailNotAvailableException;
import com.spring.project.Exception.InvalidCredentialsException;
import com.spring.project.dto.AuthenticationResponse;
import com.spring.project.dto.AuthenticationRequest;
import com.spring.project.dto.PasswordResetRequest;
import com.spring.project.dto.RegistrationRequest;
import com.spring.project.email.EmailSender;
import com.spring.project.model.Client;
import com.spring.project.repository.ClientRepository;
import com.spring.project.service.RegistrationService;
import com.spring.project.token.ConfirmationToken;
import com.spring.project.token.PasswordResetToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
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

    public String register(RegistrationRequest request){
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new EmailNotAvailableException("Email is not valid");
        }

        boolean isValidPassword = passwordValidator.test(request.getPassword());
        if(!isValidPassword){
            throw new InvalidCredentialsException("Password do not respect the criteria");
        }

        String receivedToken = clientService.signUpClient(
                new Client.Builder(request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getClientRole()).build()
        );

        String link = "http://localhost:8080/project/auth/confirm?token=" + receivedToken;
        emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link), "Please confirm your account");
        return receivedToken;
    }

    public String sendResetPasswordEmail(PasswordResetRequest request){
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new EmailNotAvailableException("Email is not valid");
        }

        Client foundClient = clientRepository.findByEmail(request.getEmail()).orElse(null);
        if(!foundClient.getEnabled()){
            throw new EmailNotAvailableException("Client does not exist yet");
        }

        String resetToken = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(
                resetToken,
                foundClient
        );
        passwordResetTokenServiceImpl.savePasswordResetToken(passwordResetToken);
        String link = "http://localhost:8080/project/auth/confirmResetToken?resetToken=" + resetToken;
        emailSender.send(request.getEmail(), buildResetPasswordEmail(request.getEmail(), link), "Reset your password");
        return resetToken;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
            var client = clientRepository.findByEmail(authenticationRequest.getEmail())
                    .orElseThrow();
            String jwt = jwtService.generateToken(client.getEmail(), String.valueOf(client.getClientRole()));
            String refreshJwt = jwtService.generateRefreshToken(client.getEmail(), String.valueOf(client.getClientRole()));
            return AuthenticationResponse.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshJwt)
                    .build();
    }

    @Transactional
    public String confirmToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenServiceImpl.getToken(token).orElse(null);

        if(confirmationToken == null){
            throw new EmailNotAvailableException("Email-ul de confirmare nu mai este valabil.");

        }
        else if(confirmationToken.getConfirmedAt() != null){
            throw new EmailNotAvailableException("Email already confirmed.");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new EmailNotAvailableException("Email expired. Please request a new one.");
        }
        confirmationTokenServiceImpl.setConfirmedAt(token);
        clientService.enableClient(
                confirmationToken.getClient().getEmail());
        return "Account was confirmed. Please log in.";
    }

    @Transactional
    public String confirmPasswordResetToken(String token) {

        PasswordResetToken passwordResetToken =  passwordResetTokenServiceImpl.getToken(token).orElse(null);

        if(passwordResetToken == null){
            throw new EmailNotAvailableException("Email-ul de confirmare nu mai este valabil");
        }
        else if(passwordResetToken.getConfirmedAt() != null){
                throw  new EmailNotAvailableException("Verification link already confirmed");
        }

        LocalDateTime expiredAt = passwordResetToken.getExpiredAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new EmailNotAvailableException("Verification link already expired");
        }
        passwordResetTokenServiceImpl.setConfirmedAt(token);
        return "Request was confirmed. Please change your password";
    }


    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    private String buildResetPasswordEmail(String email, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + email + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }



    public void updateClientPassword(Client client, String newPassword) {
        boolean isValidPassword = passwordValidator.test(newPassword);
        if(!isValidPassword){
            throw new InvalidCredentialsException("Password must respect the criteria");
        }
        clientService.resetClientPassword(client,newPassword);
    }

    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshJwt;
        final String clientEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshJwt = authHeader.substring(7);
        clientEmail = jwtService.extractClientUsername(refreshJwt);
        if (clientEmail != null){
            var clientDetails = this.clientRepository.findByEmail(clientEmail).orElseThrow();
            if(jwtService.isTokenValid(refreshJwt, clientDetails)){
                var accessToken = jwtService.generateToken(clientDetails.getEmail(), String.valueOf(clientDetails.getClientRole()));
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshJwt)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }

    }
}
