package com.spring.project.service;

import com.spring.project.Exception.EmailNotAvailableException;
import com.spring.project.Exception.InvalidCredentialsException;
import com.spring.project.dto.CreateSubscriptionRequest;
import com.spring.project.dto.TrainerRequest;
import com.spring.project.dto.TrainerResponse;
import com.spring.project.email.EmailSender;
import com.spring.project.model.Client;
import com.spring.project.model.ClientRole;
import com.spring.project.model.FotballInsideReservation;
import com.spring.project.model.Subscription;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AdminService {

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;
    private final ClientService clientService;
    private final EmailSender emailSender;
    private final ReservationService reservationService;
    private final SubscriptionService subscriptionService;

    public TrainerResponse createTrainer(TrainerRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            boolean isValidEmail = emailValidator.test(request.getEmail());
            if (!isValidEmail) {
                throw new EmailNotAvailableException("Email is not valid");
            }
            boolean isValidPassword = passwordValidator.test(request.getPassword());
            if (!isValidPassword) {
                throw new InvalidCredentialsException("Password do not respect the criteria");
            }
            String receivedToken = clientService.signUpClient(
                    new Client.Builder(request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPassword(),
                            ClientRole.TRAINER).build()
            );
            String link = "http://localhost:8080/project/auth/confirm?token=" + receivedToken;
            emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link));
            TrainerResponse trainerResponse = new TrainerResponse();
            trainerResponse.setEmail(request.getEmail());
            trainerResponse.setLastName(request.getLastName());
            trainerResponse.setFirstName(request.getFirstName());
            return trainerResponse;
        } else {
            throw new InvalidCredentialsException("Username is not loged In");
        }
    }

    public List<Client> getAllClients(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return clientService.getAllClients();
        }
        return null;
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

    public List<FotballInsideReservation> getAllReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT"))) {
                return reservationService.getAllReservations();
            }
        return null;
        }

    public void createSubscription(CreateSubscriptionRequest createSubscriptionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            Subscription subscription = new Subscription(
                    createSubscriptionRequest.getSubscriptionName(),
                    createSubscriptionRequest.getSubscriptionPrice(),
                    createSubscriptionRequest.getSubscriptionTime(),
                    createSubscriptionRequest.getSubscriptionDescription()
            );
            subscriptionService.saveSubscription(subscription);
        }
    }

    public void updateSubscription(Integer id, Map<String, Object> fields) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            Subscription subscription = subscriptionService.findById(id).orElse(null);

            fields.forEach((key, value) -> {
                switch (key) {
                    case "subscriptionName":
                        subscription.setSubscriptionName((String) value);
                        break;

                    case "subscriptionPrice":
                        subscription.setSubscriptionPrice((Double) value);
                        break;

                    case "subscriptionTime":
                        subscription.setSubscriptionTime((Integer) value);
                        break;

                    case "subscriptionDescription":
                        subscription.setSubscriptionDescription((String) value);
                        break;

                    default:
                        throw new IllegalArgumentException("Invalid field:" + key);
                }
            });
            subscriptionService.saveSubscription(subscription);
        }
    }

    public void deleteSubscription(Integer id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            subscriptionService.deleteSubscription(id);
        }
    }
}
