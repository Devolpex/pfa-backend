package com.pfa.pfabackend.auth.services;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.pfa.pfabackend.auth.enums.Auth;
import com.pfa.pfabackend.auth.https.req.LoginRequest;
import com.pfa.pfabackend.auth.https.req.RegisterRequest;
import com.pfa.pfabackend.auth.https.res.AuthResponse;
import com.pfa.pfabackend.basic.BasicException;
import com.pfa.pfabackend.basic.BasicResponse;
import com.pfa.pfabackend.basic.BasicValiadtion;
import com.pfa.pfabackend.client.Client;
import com.pfa.pfabackend.client.ClientRepository;
import com.pfa.pfabackend.messages.Message;
import com.pfa.pfabackend.messages.enums.MessageType;
import com.pfa.pfabackend.token.JwtService;
import com.pfa.pfabackend.user.UserDTO;
import com.pfa.pfabackend.user.data.CodeConfirmationRepository;
import com.pfa.pfabackend.user.data.UserRepository;
import com.pfa.pfabackend.user.enums.Role;
import com.pfa.pfabackend.user.models.CodeConfirmation;
import com.pfa.pfabackend.user.models.User;
import com.pfa.pfabackend.user.services.UserService;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CodeConfirmationRepository codeConfirmationRepository;
    private final BasicValiadtion basicValiadtion;

    private final UserService userService;
    private final PasswordService passwordService;

    public BasicResponse registerclient(@Valid RegisterRequest request, BindingResult bindingResult)
            throws BasicException {
        Map<String, String> errors = new HashMap<>();
        // Request validation
        if (bindingResult.hasErrors()) {
            BasicResponse resposne = BasicResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(basicValiadtion.handleValidationErrors(bindingResult))
                    .build();
            throw new BasicException(resposne);
        }
        // Check if the email already exists
        if (userService.emailExists(request.getEmail())) {
            errors.put("email", "Email already exists");
            BasicResponse response = BasicResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(Message.builder().messages(errors)
                            .type(MessageType.ERROR).build())
                    .build();
            throw new BasicException(response);
        }
        // Check if username already exists
        if (userService.usernameExists(request.getUsername())) {
            errors.put("email", "Email already exists");
            BasicResponse response = BasicResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(Message.builder().messages(errors)
                            .type(MessageType.ERROR).build())
                    .build();
            throw new BasicException(response);
        }
        // Check password confirmation
        if (!userService.confirmPassword(request.getPassword(), request.getConfirmPassword())) {
            errors.put("confirmPassword", "Password and confirm password are not the same");
            BasicResponse response = BasicResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(Message.builder().messages(errors)
                            .type(MessageType.ERROR).build())
                    .build();
            throw new BasicException(response);
        }

        // Encode the password
        request.setPassword(passwordService.bcryptPassword(request.getPassword()));
        // Format the data
        request.setEmail(request.getEmail().toLowerCase());
        request.setUsername(request.getUsername().toLowerCase());
        request.setFirstname(userService.firstLetterToUpperCase(request.getFirstname()));
        request.setLastname(userService.firstLetterToUpperCase(request.getLastname()));
        // Build the DTO
        UserDTO userDTO = buildRegisterRequestToDTO(request);
        // Generate the token
        String token = jwtService.createToken(userDTO);
        // Save client
        userService.saveUser(userDTO);
        // return success response
        BasicResponse successResponse = BasicResponse.builder()
                .status(HttpStatus.OK)
                .message(Message.builder().message("Registration successful").type(MessageType.SUCCESS).build())
                .data(Map.of("token", token))
                .build();
        return successResponse;

    }

    // public AuthResponse register(User user) {
    //     repository.save(user);
    //     var jwtToken = jwtService.generateToken(user);
    //     Client client = Client.builder().user(user).auth(Auth.EMAIL).build();
    //     clientRepository.save(client);
    //     return AuthResponse.builder().token(jwtToken).role(user.getRole()).build();
    // }

    public User login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));
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
    // CodeConfirmation codeConfirmation =
    // codeConfirmationRepository.findLastCodeConfirmationByClientEmail(email);
    // return codeConfirmation;
    // }

    public CodeConfirmation getLastCodeConfirmationByEmail(String email) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("id").descending());
        List<CodeConfirmation> confirmations = codeConfirmationRepository.findLastCodeConfirmationByClientEmail(email,
                pageable);
        return confirmations.isEmpty() ? null : confirmations.get(0);
    }

    public boolean confirmCodeConfirmation(String clientCodeConfirmation, String systemCodeConfirmation) {
        if (clientCodeConfirmation.equals(systemCodeConfirmation)) {
            return true;
        }
        return false;
    }

    public void deleteCodeConfirmation(String email) {
        codeConfirmationRepository.deleteCodeConfirmationsByClientEmail(email);
    }

    public boolean comparePasswords(String rawPassword, String encodedPassword) {
        // Compare raw password with encoded (hashed) password
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private UserDTO buildRegisterRequestToDTO(RegisterRequest request) {
        return UserDTO.builder()
                .email(request.getEmail())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .username(request.getUsername())
                .password(request.getPassword())
                .role(Role.CLIENT)
                .build();
    }

}
