package com.spring.project.service;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(passwordRegex);

        // Create matcher object
        Matcher matcher = pattern.matcher(s);

        // Return true if the input string matches the password pattern
        return matcher.matches();
    }
}
