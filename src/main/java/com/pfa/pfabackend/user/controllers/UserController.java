package com.pfa.pfabackend.user.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.pfa.pfabackend.basic.BasicException;
import com.pfa.pfabackend.basic.BasicResponse;
import com.pfa.pfabackend.messages.Message;
import com.pfa.pfabackend.token.JwtService;
import com.pfa.pfabackend.user.https.ProfileResponse;
import com.pfa.pfabackend.user.https.req.UpdatePasswordRequest;
import com.pfa.pfabackend.user.https.req.UpdateUserInfosRequest;
import com.pfa.pfabackend.user.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    // Get user infos by email
    @GetMapping("/user-infos")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    public ResponseEntity<BasicResponse> getUserInfos(@RequestParam String token) {
        try {
            BasicResponse response = userService.getUserInfos(token);
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

    // @PutMapping("/update-password")
    // @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    // public ResponseEntity<BasicResponse> updatePassword(
    // @RequestBody @Valid UpdatePasswordRequest request, BindingResult
    // bindingResult) {
    // List<String> errors = new ArrayList<>();
    // if (bindingResult.hasErrors()) {
    // errors = bindingResult.getAllErrors().stream().map(error ->
    // error.getDefaultMessage())
    // .collect(Collectors.toList());
    // return
    // ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BasicResponse.builder().errors(errors).build());
    // }

    // try {
    // Message message = userService.changePassword(request);
    // return
    // ResponseEntity.status(HttpStatus.OK).body(BasicResponse.builder().message(message).build());
    // } catch (BasicException e) {
    // return ResponseEntity.status(e.getStatus())
    // .body(BasicResponse.builder().errors(Collections.singletonList(e.getMessage())).build());
    // }
    // }

    @PutMapping("/update-infos")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    public ResponseEntity<BasicResponse> updateUserInfos(
            @RequestBody @Valid UpdateUserInfosRequest request,@RequestParam String token, BindingResult bindingResult) {
        try {
            BasicResponse response = userService.updateUserInfos(request, bindingResult, token);
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

}