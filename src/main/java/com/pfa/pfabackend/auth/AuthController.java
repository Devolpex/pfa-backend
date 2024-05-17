package com.pfa.pfabackend.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.pfa.pfabackend.auth.https.req.CodeValidationRequest;
import com.pfa.pfabackend.auth.https.req.ForgetPasswordRequest;
import com.pfa.pfabackend.auth.https.req.LoginRequest;
import com.pfa.pfabackend.auth.https.req.NewPasswordRequest;
import com.pfa.pfabackend.auth.https.req.RegisterRequest;
import com.pfa.pfabackend.auth.https.res.AuthResponse;
import com.pfa.pfabackend.auth.https.res.CodeValidationResponse;
import com.pfa.pfabackend.auth.https.res.ForgetPasswordResponse;
import com.pfa.pfabackend.auth.https.res.LoginResponse;
import com.pfa.pfabackend.auth.https.res.NewPasswordResponse;
import com.pfa.pfabackend.auth.services.AuthService;
import com.pfa.pfabackend.basic.BasicException;
import com.pfa.pfabackend.basic.BasicResponse;
import com.pfa.pfabackend.basic.BasicValiadtion;
import com.pfa.pfabackend.client.Client;
import com.pfa.pfabackend.client.ClientService;
import com.pfa.pfabackend.email.EmailService;
import com.pfa.pfabackend.messages.Message;
import com.pfa.pfabackend.messages.enums.MessageType;
import com.pfa.pfabackend.token.JwtService;
import com.pfa.pfabackend.user.UserDTO;
import com.pfa.pfabackend.user.enums.Role;
import com.pfa.pfabackend.user.models.CodeConfirmation;
import com.pfa.pfabackend.user.models.User;
import com.pfa.pfabackend.user.services.CodeConfirmationService;
import com.pfa.pfabackend.user.services.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;
    private final ClientService clientService;
    private final EmailService emailService;
    private final CodeConfirmationService codeConfirmationService;

    private final BasicValiadtion basicValiadtion;

    // Register a new user

    @PostMapping("/register")
    public ResponseEntity<BasicResponse> register(@Valid @RequestBody RegisterRequest request,
            BindingResult bindingResult) {
        try {
            BasicResponse response = authService.registerclient(request, bindingResult);
            return ResponseEntity.status(response.getStatus())
                    .body(BasicResponse.builder()
                            .status(response.getStatus())
                            .message(response.getMessage())
                            .data(response.getData())
                            .redirectTo(response.getRedirectTo())
                            .build());
        } catch (BasicException e) {
            return ResponseEntity.status(e.getResponse().getStatus())
                    .body(e.getResponse());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<BasicResponse> login(@Valid @RequestBody LoginRequest request,
            BindingResult bindingResult) {
        try {
            BasicResponse response = authService.login(request, bindingResult);
            return ResponseEntity.status(response.getStatus())
                    .body(BasicResponse.builder()
                            .status(response.getStatus())
                            .message(response.getMessage())
                            .data(response.getData())
                            .redirectTo(response.getRedirectTo())
                            .build());
        } catch (BasicException e) {
            return ResponseEntity.status(e.getResponse().getStatus())
                    .body(e.getResponse());
        }
    }

    // Login a user
    // @PostMapping("/login")
    // public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request, BindingResult bindingResult) {
    //     List<String> errors = new ArrayList<>();

    //     if (bindingResult.hasErrors()) {
    //         errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
    //                 .collect(Collectors.toList());
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponse.builder().errors(errors).build());
    //     }
    //     try {
    //         UserDTO user = authService.login(request);
    //         if (user == null) {
    //             throw new BadCredentialsException("Invalid email or password");
    //         } else {
    //             var jwtToken = jwtService.createToken(user);
    //             String redirectTo = new String();
    //             if (user.getRole() == Role.CLIENT) {
    //                 redirectTo = "/profile";
    //             }
    //             if (user.getRole() == Role.ADMIN) {
    //                 redirectTo = "/clients";
    //             }

    //             return ResponseEntity.ok(LoginResponse.builder()
    //                     .token(jwtToken)
    //                     .role(user.getRole())
    //                     .success("Login successful")
    //                     .redirectTo(redirectTo)
    //                     .build());
    //         }

    //     } catch (BadCredentialsException e) {
    //         String errorMessage = e.getMessage();
    //         if (errorMessage != null) {
    //             // Check if the email exists in the database
    //             String userPassword = userService.findPasswordByEmail(request.getEmail());
    //             if (userService.emailExists(request.getEmail()) == false) {
    //                 errors.add("Email does not exist");
    //             } else if (authService.comparePasswords(request.getPassword(), userPassword) == false) {
    //                 // Email exists but password is incorrect
    //                 errors.add("Incorrect password");
    //             }
    //         }
    //         if (!errors.isEmpty()) {
    //             // Return errors in the response
    //             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //                     .body(LoginResponse.builder().errors(errors).build());
    //         } else {
    //             // If no specific errors are detected, return a generic error
    //             errors.add(errorMessage);
    //             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //                     .body(LoginResponse.builder().errors(errors).build());
    //         }
    //     }
    // }

    // Forget password
    @PostMapping("/forget-password")
    public ResponseEntity<ForgetPasswordResponse> forgetPassword(@RequestBody @Valid ForgetPasswordRequest request,
            BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
        }
        if (!userService.emailExists(request.getEmail())) {
            errors.add("This email don't exists");
        }
        if (!clientService.checkIsAuthByEmail(request.getEmail())) {
            errors.add("This email Register by Facebook or Google");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(ForgetPasswordResponse.builder().errors(errors).build());
        }
        String code_validation = authService.generateCodeValidation();
        Client client = clientService.findClientByEmail(request.getEmail());
        if (client != null) {
            Thread thread = new Thread(() -> {
                CodeConfirmation codeConfirmation = CodeConfirmation.builder()
                        .expiration_date(new Date(System.currentTimeMillis() + (10 * 60 * 1000))) // Adding 10 minutes
                                                                                                  // in milliseconds
                        .code(code_validation)
                        .client(client)
                        .build();
                codeConfirmationService.saveCodeConfirmation(codeConfirmation);
            });
            thread.start();
        }

        String text = "Your code validation is " + code_validation;
        emailService.sendEmail(request.getEmail(), "Rest Password Code Confirmation", text);

        return ResponseEntity.ok().body(ForgetPasswordResponse.builder()
                .success("Code Validation sent succefully. Check you email " + request.getEmail())
                .redirectTo("/code-validation").build());
    }

    // Code validation
    @PostMapping("/code-validation")
    public ResponseEntity<CodeValidationResponse> codeValidation(@RequestBody @Valid CodeValidationRequest request,
            BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
        }
        Client client = clientService.findClientByEmail(request.getEmail());
        if (client == null) {
            errors.add("Client don't exists");
        }
        // CodeConfirmation codeConfirmation =
        // authService.getCodeConfirmation(request.getEmail());
        CodeConfirmation codeConfirmation = authService.getLastCodeConfirmationByEmail(request.getEmail());
        if (!authService.confirmCodeConfirmation(request.getCode(), codeConfirmation.getCode())) {
            errors.add("The code confirmation is not valid");
        }
        if (codeConfirmation.getExpiration_date().before(new Date())) {
            errors.add("This code is expired. Get new code confirmation");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(CodeValidationResponse.builder().errors(errors).build());
        }
        String clientEmail = codeConfirmation.getClient().getUser().getEmail();
        // authService.deleteCodeConfirmation(clientEmail);
        return ResponseEntity.ok().body(CodeValidationResponse
                .builder()
                .success("Your code is valid")
                .email(clientEmail)
                .redirectTo("/new-password")
                .build());

    }

    // New password
    @PostMapping("/new-password")
    public ResponseEntity<NewPasswordResponse> newPassword(@RequestBody NewPasswordRequest request,
            BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
        }

        if (!userService.emailExists(request.getEmail())) {
            errors.add("Email don't exists");
        }

        if (!userService.confirmPassword(request.getPassword(), request.getConfirm_password())) {
            errors.add("Password not mutch with the confirm password");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(NewPasswordResponse.builder().errors(errors).build());
        }
        userService.bcryptPassword(request.getPassword());
        return ResponseEntity.ok().body(NewPasswordResponse.builder()
                .success("Password changed succefuly, try login now")
                .redirectTo("/login")
                .build());
    }
}
