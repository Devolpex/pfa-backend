package com.pfa.pfabackend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pfa.pfabackend.dto.user.UserDTO;
import com.pfa.pfabackend.request.user.ProfileRequest;
import com.pfa.pfabackend.response.user.ProfileResponse;
import com.pfa.pfabackend.service.JwtService;
import com.pfa.pfabackend.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ADMIN', 'CLIENT')")
    public ResponseEntity<ProfileResponse> getUserInfosByUsername(@RequestBody ProfileRequest request) {
        String token = request.getToken();
        String userEmail = jwtService.extractUsername(token);
        if (userEmail != null) {
            try {
                UserDTO user = userService.findUserByEmail(userEmail);
                return ResponseEntity.status(HttpStatus.OK).body(ProfileResponse.builder().user(user).build());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ProfileResponse.builder().errors(Collections.singletonList(e.getMessage())).build());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ProfileResponse.builder().errors(Collections.singletonList("Unauthorized")).build());
    }

    

}



