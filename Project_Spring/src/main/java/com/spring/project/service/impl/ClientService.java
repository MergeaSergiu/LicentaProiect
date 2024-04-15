package com.spring.project.service.impl;

import com.spring.project.mapper.ConfirmationTokenMapper;
import com.spring.project.model.User;
import com.spring.project.repository.ClientRepository;
import com.spring.project.service.ConfirmationTokenService;
import com.spring.project.token.ConfirmationToken;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService implements UserDetailsService {


    private PasswordEncoder passwordEncoder;
    private final static String CLIENT_NOT_FOUND_ERROR = "Client with email %s not found";
    private final ClientRepository clientRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenMapper confirmationTokenMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clientRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(String.format(CLIENT_NOT_FOUND_ERROR, email)));
    }
    public String signUpClient(User user) {
        User userAlreadyExist = clientRepository.findByEmail(user.getEmail()).orElse(null);
        if(userAlreadyExist == null){
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = confirmationTokenMapper.createConfirmationToken(token,LocalDateTime.now(), user);
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            clientRepository.save(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            return token;
        }
            if (userAlreadyExist.getEnabled()) {
                throw new EntityExistsException("Account already exist");
            }else {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                userAlreadyExist.setFirstName(user.getFirstName());
                userAlreadyExist.setLastName(user.getLastName());
                userAlreadyExist.setPassword(encodedPassword);
                clientRepository.save(userAlreadyExist);

                String token = UUID.randomUUID().toString();
                ConfirmationToken foundConfirmationToken = confirmationTokenService.findTokenByUserId(userAlreadyExist.getId());
                foundConfirmationToken.setToken(token);
                foundConfirmationToken.setConfirmedAt(null);
                foundConfirmationToken.setCreatedAt(LocalDateTime.now());
                foundConfirmationToken.setExpiredAt(LocalDateTime.now().plusMinutes(60));
                foundConfirmationToken.setUser(userAlreadyExist);
                confirmationTokenService.saveConfirmationToken(foundConfirmationToken);
                return token;
            }
    }

    public void resetClientPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        clientRepository.save(user);
    }

}
