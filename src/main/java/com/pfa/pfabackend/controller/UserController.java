package com.pfa.pfabackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.pfa.pfabackend.Exception.UserNotFoundException;
import com.pfa.pfabackend.dto.user.UserCreateDto;
import com.pfa.pfabackend.dto.user.UserUpdateDto;
import com.pfa.pfabackend.model.User;
import com.pfa.pfabackend.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDto userCreateDto, BindingResult bindingResult){
        Map<String, Object> errorsResponse = new HashMap<>();
        List<String> validationsErrors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            validationsErrors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        }
        if (service.emailExists(userCreateDto.getEmail())){
            validationsErrors.add("Email alredy exists");
        }
        if (!service.confirmPassword(userCreateDto.getPassword(),userCreateDto.getConfirm_password())){
            validationsErrors.add("Password confirmation not mush");
        }
        if (!validationsErrors.isEmpty()){
            errorsResponse.put("errors",validationsErrors);
            return ResponseEntity.badRequest().body(errorsResponse);
        }
        service.saveUser(userCreateDto);
        Map<String,String> successMessage = new HashMap<>();
        successMessage.put("success","User created successfuly");
        return ResponseEntity.ok(successMessage);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(service.findAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        service.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
    //Handle the Custom Exception if id Non-Existent
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(UserNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public String handleUserNotFoundException(UserNotFoundException ex) {
            return ex.getMessage();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody @Valid UserUpdateDto userUpdateDto, BindingResult bindingResult) {
        Map<String, Object> errorsResponse = new HashMap<>();
        List<String> validationsErrors = new ArrayList<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (bindingResult.hasErrors()) {
            validationsErrors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        }
        // Check if passwords match and hash the new password
        if (!userUpdateDto.getPassword().isEmpty()) {
            if (userUpdateDto.getConfirm_password().isEmpty()) {
                validationsErrors.add("Confirm password is required");
            } else if (!service.confirmPassword(userUpdateDto.getPassword(), userUpdateDto.getConfirm_password())) {
                validationsErrors.add("Password confirmation does not match");
            } else {
                userUpdateDto.setPassword(encoder.encode(userUpdateDto.getPassword()));
            }
        }
        if (!validationsErrors.isEmpty()){
            errorsResponse.put("errors",validationsErrors);
            return ResponseEntity.badRequest().body(errorsResponse);
        }
        // Hash password if it's not empty
        if (!service.PasswordIsEmpty(userUpdateDto.getPassword(), userUpdateDto.getConfirm_password())) {
            encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(userUpdateDto.getPassword());
            userUpdateDto.setPassword(hashedPassword);
        }
        service.updateUser(id, userUpdateDto);
        Map<String,String> successMessage = new HashMap<>();
        successMessage.put("success","User updated successfuly");
        return ResponseEntity.ok(successMessage);
    }
}

