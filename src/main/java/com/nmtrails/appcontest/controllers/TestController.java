package com.nmtrails.appcontest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = {"http://localhost:3000"})
public class TestController {

    @GetMapping("/hi")
    public ResponseEntity<?> getGreeting() {
        return ResponseEntity.ok("hello");
    }

}
