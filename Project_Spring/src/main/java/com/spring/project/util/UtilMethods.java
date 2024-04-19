package com.spring.project.util;

import com.spring.project.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Component
public class UtilMethods {

    @Autowired
    private final JwtService jwtService;

    public UtilMethods(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String loadEmailTemplateFromResource(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String extractUsernameFromAuthorizationHeader(String authorization){
        String jwt = authorization.substring(7);
        return jwtService.extractClientUsername(jwt);
    }
}
