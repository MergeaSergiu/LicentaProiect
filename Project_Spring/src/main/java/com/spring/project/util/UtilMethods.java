package com.spring.project.util;

import com.spring.project.model.User;
import com.spring.project.repository.UserRepository;
import com.spring.project.service.impl.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Component
@AllArgsConstructor
public class UtilMethods {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserRepository userRepository;


    public String loadEmailTemplateFromResource(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public User extractUsernameFromAuthorizationHeader(String authorization){
        String jwt = authorization.substring(7);
        String username = jwtService.extractClientUsername(jwt);
        return userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
    }
}
