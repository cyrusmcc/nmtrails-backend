package com.nmtrails.appcontest.controllers;

import com.nmtrails.appcontest.services.TrailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trails")
public class TrailController {
    private TrailService trailService;

    @Autowired
    public TrailController(TrailService trailService) {
        this.trailService = trailService;
    }

    @GetMapping("/")
    public ResponseEntity<?> listTrails(@RequestParam String name,
                                        @RequestParam int page,
                                        @RequestParam int pageSize) {
        return ResponseEntity.ok("");
    }
}
