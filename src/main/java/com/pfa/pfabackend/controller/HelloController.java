package com.pfa.pfabackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
    @GetMapping()
    public ResponseEntity<?> getHello(){
        return ResponseEntity.ok("Hello in marouane application");
    }
}
