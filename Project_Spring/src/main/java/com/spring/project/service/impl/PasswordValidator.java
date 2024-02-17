package com.spring.project.service.impl;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);

        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }
}
