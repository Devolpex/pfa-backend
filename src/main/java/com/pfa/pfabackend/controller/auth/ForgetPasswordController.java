package com.pfa.pfabackend.controller.auth;

import org.springframework.web.bind.annotation.*;

import com.pfa.pfabackend.model.Client;
import com.pfa.pfabackend.model.CodeConfirmation;
import com.pfa.pfabackend.request.auth.CodeValidationRequest;
import com.pfa.pfabackend.request.auth.ForgetPasswordRequest;
import com.pfa.pfabackend.request.auth.NewPasswordRequest;
import com.pfa.pfabackend.response.auth.CodeValidationResponse;
import com.pfa.pfabackend.response.auth.ForgetPasswordResponse;
import com.pfa.pfabackend.response.auth.NewPasswordResponse;
import com.pfa.pfabackend.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;


@RestController
@RequestMapping("/api/auth/psswd")
@RequiredArgsConstructor

public class ForgetPasswordController {
    private final UserService userService;
    private final ClientService clientService;
    private final AuthService authService;
    private final EmailService emailService;
    private final CodeConfirmationService codeConfirmationService;
    

    @PostMapping("/forget-password")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<ForgetPasswordResponse> forgetPassword(@RequestBody @Valid ForgetPasswordRequest request,BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        }
        if (!userService.emailExists(request.getEmail())) {
            errors.add("This email don't exists");
        }
        if (!clientService.checkIsAuthByEmail(request.getEmail())) {
            errors.add("This email Register by Facebook or Google");
        }
        if (!errors.isEmpty()){
            return ResponseEntity.badRequest().body(ForgetPasswordResponse.builder().errors(errors).build());
        }
        String code_validation = authService.generateCodeValidation();
        Client client = clientService.findClientByEmail(request.getEmail());
        if (client != null){
            Thread thread = new Thread(() -> {
                CodeConfirmation codeConfirmation = CodeConfirmation.builder()
                        .expiration_date(new Date(System.currentTimeMillis() + (10 * 60 * 1000))) // Adding 10 minutes in milliseconds
                        .code(code_validation)
                        .client(client)
                        .build();
                codeConfirmationService.saveCodeConfirmation(codeConfirmation);
            });
            thread.start();
        }

        String text = "Your code validation is " + code_validation;
        emailService.sendEmail(request.getEmail(),"Rest Password Code Confirmation",text);

        return ResponseEntity.ok().body(ForgetPasswordResponse.builder()
            .success("Code Validation sent succefully. Check you email " + request.getEmail())
            .redirectTo("/code-validation").build());
    }

    @PostMapping("/code-validation")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<CodeValidationResponse> codeValidation(@RequestBody @Valid CodeValidationRequest request, BindingResult bindingResult){
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        }
        Client client = clientService.findClientByEmail(request.getEmail());
        if (client == null){
            errors.add("Client don't exists");
        }
        // CodeConfirmation codeConfirmation = authService.getCodeConfirmation(request.getEmail());
        CodeConfirmation codeConfirmation = authService.getLastCodeConfirmationByEmail(request.getEmail());
        if (!authService.confirmCodeConfirmation(request.getCode(), codeConfirmation.getCode())){
            errors.add("The code confirmation is not valid");
        }
        if (codeConfirmation.getExpiration_date().before(new Date())){
            errors.add("This code is expired. Get new code confirmation");
        }
        if (!errors.isEmpty()){
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

    @PostMapping("/new-password")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<NewPasswordResponse> newPassword(@RequestBody NewPasswordRequest request,BindingResult bindingResult){
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        }

        if (!userService.emailExists(request.getEmail())) {
            errors.add("Email don't exists");
        }

        if (!userService.confirmPassword(request.getPassword(), request.getConfirm_password())) {
            errors.add("Password not mutch with the confirm password");
        }

        if (!errors.isEmpty()){
            return ResponseEntity.badRequest().body(NewPasswordResponse.builder().errors(errors).build());
        }
        userService.bcryptPassword(request.getPassword());
        return ResponseEntity.ok().body(NewPasswordResponse.builder()
                .success("Password changed succefuly, try login now")
                .redirectTo("/login")
                .build()
        );
    }
}
