package com.pfa.pfabackend.auth.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final PasswordEncoder passwordEncoder; 

    // Check if the current password is valid
    public boolean checkPassword(String rawPassword,String encodePassword){
        return passwordEncoder.matches(rawPassword,encodePassword);
    }

    // Confirm if the password and confirm password are the same
    public boolean confirmPassword(String password, String confirm) {
        return password.equals(confirm);
    }

    // Bcrypt password
    public String bcryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
    
}
