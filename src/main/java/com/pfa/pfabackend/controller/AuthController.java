package com.pfa.pfabackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.Binding;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.pfa.pfabackend.enums.Role;
import com.pfa.pfabackend.model.User;
import com.pfa.pfabackend.request.auth.LoginRequest;
import com.pfa.pfabackend.request.auth.RegisterRequest;
import com.pfa.pfabackend.response.auth.AuthResponse;
import com.pfa.pfabackend.response.auth.LoginResponse;
import com.pfa.pfabackend.service.AuthService;
import com.pfa.pfabackend.service.JwtService;
import com.pfa.pfabackend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request,
            BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthResponse.builder().errors(errors).build());
        }
        if (userService.emailExists(request.getEmail())) {
            errors.add("Email already exists");
        }
        if (!userService.confirmPassword(request.getPassword(), request.getConfirm_password())) {
            errors.add("Password confirmation does not match");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthResponse.builder().errors(errors).build());
        }
        User user = User.builder()
                .firstname(userService.firstLetterToUpperCase(request.getFirstname()))
                .lastname(userService.firstLetterToUpperCase(request.getLastname()))
                .email(request.getEmail().toUpperCase())
                .phone(request.getPhone())
                .password(userService.bcryptPassword(request.getPassword()))
                .role(Role.CLIENT)
                .created_at(new Date())
                .build();
        AuthResponse response = authService.register(user);
        response.setSuccess("Registration successful");
        response.setRedirectTo("/profile");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request, BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponse.builder().errors(errors).build());
        }
        try {
            User user = authService.login(request);
            if (user == null) {
                throw new BadCredentialsException("Invalid email or password");
            } else {
                var jwtToken = jwtService.generateToken(user);
                String redirectTo = new String();
                if (user.getRole() == Role.CLIENT) {
                    redirectTo = "/profile";
                }
                if (user.getRole() == Role.ADMIN) {
                    redirectTo = "/clients";
                }

                return ResponseEntity.ok(LoginResponse.builder()
                        .token(jwtToken)
                        .role(user.getRole())
                        .success("Login successful")
                        .redirectTo(redirectTo)
                        .build());
            }

        } catch (BadCredentialsException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null) {
                // Check if the email exists in the database
                String userPassword = userService.findPasswordByEmail(request.getEmail());
                if (userService.emailExists(request.getEmail()) == false) {
                    errors.add("Email does not exist");
                } else if (authService.comparePasswords(request.getPassword(), userPassword) == false) {
                    // Email exists but password is incorrect
                    errors.add("Incorrect password");
                }
            }
            if (!errors.isEmpty()) {
                // Return errors in the response
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(LoginResponse.builder().errors(errors).build());
            } else {
                // If no specific errors are detected, return a generic error
                errors.add(errorMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(LoginResponse.builder().errors(errors).build());
            }
        }
    }
}
