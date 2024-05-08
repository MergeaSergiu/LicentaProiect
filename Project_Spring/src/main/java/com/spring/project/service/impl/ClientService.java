package com.spring.project.service.impl;

import com.spring.project.mapper.ConfirmationTokenMapper;
import com.spring.project.model.User;
import com.spring.project.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenMapper confirmationTokenMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
    }

    public String signUpClient(User user) {

        User userAlreadyExist = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userAlreadyExist == null) {
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = confirmationTokenMapper.createConfirmationToken(token, LocalDateTime.now(), user);
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setEnabled(false);
            userRepository.save(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            return token;
        }
        if (userAlreadyExist.getEnabled()) {
            throw new EntityExistsException("An account with this email already exist");
        } else {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            userAlreadyExist.setFirstName(user.getFirstName());
            userAlreadyExist.setLastName(user.getLastName());
            userAlreadyExist.setPassword(encodedPassword);
            userAlreadyExist.setEnabled(false);
            userRepository.save(userAlreadyExist);

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
        userRepository.save(user);
    }

}
