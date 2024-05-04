package com.pfa.pfabackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfa.pfabackend.request.client.ProfilePictureReq;
import com.pfa.pfabackend.response.client.ProfilePictureRes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/clients/profile-picture")
@RequiredArgsConstructor
public class ProfilePictureController {

    @PostMapping
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<ProfilePictureRes> addProfilePicture(@RequestBody @Valid ProfilePictureReq  request,BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        }
        if (!errors.isEmpty()){
            return ResponseEntity.badRequest().body(ProfilePictureRes.builder().errors(errors).build());
        }
        return ResponseEntity.ok(ProfilePictureRes.builder()
            .picture(request.getPicture())
            .success("success")
            .build());
    }

}
