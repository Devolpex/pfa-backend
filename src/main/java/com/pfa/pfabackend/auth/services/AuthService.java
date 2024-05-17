package com.pfa.pfabackend.auth.services;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pfa.pfabackend.auth.enums.Auth;
import com.pfa.pfabackend.auth.https.req.LoginRequest;
import com.pfa.pfabackend.auth.https.req.RegisterRequest;
import com.pfa.pfabackend.auth.https.res.AuthResponse;
import com.pfa.pfabackend.client.Client;
import com.pfa.pfabackend.client.ClientRepository;
import com.pfa.pfabackend.token.JwtService;
import com.pfa.pfabackend.user.data.CodeConfirmationRepository;
import com.pfa.pfabackend.user.data.UserRepository;
import com.pfa.pfabackend.user.enums.Role;
import com.pfa.pfabackend.user.models.CodeConfirmation;
import com.pfa.pfabackend.user.models.User;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CodeConfirmationRepository codeConfirmationRepository;

    public AuthResponse register(User user) {
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        Client client = Client.builder().user(user).auth(Auth.EMAIL).build();
        clientRepository.save(client);
        return AuthResponse.builder().token(jwtToken).role(user.getRole()).build();
    }

    public User login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        // var jwtToken = jwtService.generateToken(user);
        // return AuthResponse.builder().token(jwtToken).role(user.getRole()).build();
        return user;
    }

    public String generateCodeValidation() {
        // Generate a random 6-digit code
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int code = random.nextInt(max - min + 1) + min;
        return String.valueOf(code);
    }

    // public CodeConfirmation getCodeConfirmation(String email){
    //     CodeConfirmation codeConfirmation = codeConfirmationRepository.findLastCodeConfirmationByClientEmail(email);
    //     return codeConfirmation;
    // }

    public CodeConfirmation getLastCodeConfirmationByEmail(String email) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("id").descending());
        List<CodeConfirmation> confirmations = codeConfirmationRepository.findLastCodeConfirmationByClientEmail(email, pageable);
        return confirmations.isEmpty() ? null : confirmations.get(0);
    }
    
    public boolean confirmCodeConfirmation(String clientCodeConfirmation,String systemCodeConfirmation){
        if (clientCodeConfirmation.equals(systemCodeConfirmation)){
            return true;
        }
        return false;
    }

    public void deleteCodeConfirmation(String email){
        codeConfirmationRepository.deleteCodeConfirmationsByClientEmail(email);
    }
    public boolean comparePasswords(String rawPassword, String encodedPassword) {
        // Compare raw password with encoded (hashed) password
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }



}
