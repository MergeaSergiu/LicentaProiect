package com.spring.project.service;

import com.spring.project.Exception.ClientNotFoundException;
import com.spring.project.Exception.InvalidCredentialsException;
import com.spring.project.model.Client;
import com.spring.project.repository.ClientRepository;
import com.spring.project.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService implements UserDetailsService {

    private final static String CLIENT_NOT_FOUND_ERROR =
            "Client with email %s not found";
    private final ClientRepository clientRepository;

    private final ConfirmationTokenService confirmationTokenService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clientRepository.findByEmail(email).orElseThrow(() -> new ClientNotFoundException(String.format(CLIENT_NOT_FOUND_ERROR,email)));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String signUpClient(Client client){
        boolean userExists = clientRepository.findByEmail(client.getEmail())
                .isPresent();
        if(userExists) {
            if (clientRepository.findByEmail(client.getEmail()).orElse(null).getEnabled()) {
                throw new InvalidCredentialsException("Account already exist");
            } else {
                Client alreadyExistClient = clientRepository.findByEmail(client.getEmail()).orElseThrow(() -> new ClientNotFoundException("Client does not exist"));
                String encodedPassword = passwordEncoder().encode(client.getPassword());

                alreadyExistClient.setFirstName(client.getFirstName());
                alreadyExistClient.setLastName(client.getLastName());
                alreadyExistClient.setPassword(encodedPassword);
                clientRepository.save(alreadyExistClient);

                String token = UUID.randomUUID().toString();
                ConfirmationToken foundConfirmationToken = confirmationTokenService.findTokenByUserId(Math.toIntExact(alreadyExistClient.getId()));
                foundConfirmationToken.setToken(token);
                foundConfirmationToken.setConfirmedAt(null);
                foundConfirmationToken.setCreatedAt(LocalDateTime.now());
                foundConfirmationToken.setExpiredAt(LocalDateTime.now().plusMinutes(60));
                foundConfirmationToken.setClient(alreadyExistClient);
                confirmationTokenService.saveConfirmationToken(foundConfirmationToken);

                return token;
            }
        }
        else {
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(60),
                    client
            );
            String encodedPassword = passwordEncoder()
                    .encode(client.getPassword());
            client.setPassword(encodedPassword);
            clientRepository.save(client);
            confirmationTokenService.saveConfirmationToken(confirmationToken);

            return token;
        }
    }

    public int enableClient(String email){
        return clientRepository.enableClient(email);
    }

    public void resetClientPassword(Client client, String newPassword){
        client.setPassword(passwordEncoder().encode(newPassword));
        clientRepository.save(client);
    }
}
