package com.pfa.pfabackend.user.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.pfa.pfabackend.basic.BasicException;
import com.pfa.pfabackend.basic.BasicResponse;
import com.pfa.pfabackend.user.https.req.PictureRequest;
import com.pfa.pfabackend.user.services.PictureService;

import io.micrometer.core.ipc.http.HttpSender.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import javax.naming.Binding;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/picture")
@CrossOrigin(origins = "http://localhost:3000")

public class PictureController {

    private final PictureService pictureService;
    @PostMapping("/profile")
    public ResponseEntity<BasicResponse> uploadProfilePicture(@RequestBody @Valid PictureRequest request,
            BindingResult bindingResult, @RequestParam String token) throws IOException {
        try {
            BasicResponse response = pictureService.uploadPicture(request, bindingResult, token);
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
