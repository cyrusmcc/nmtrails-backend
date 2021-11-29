package com.nmtrails.appcontest.controllers;

import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.services.TrailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trails")
public class TrailController {
    private TrailService trailService;

    @Autowired
    public TrailController(TrailService trailService) {
        this.trailService = trailService;
    }

    @GetMapping("/")
    public List<Trail> listTrails(@RequestParam(required = false) String name,
                                  @RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageRequest pr = PageRequest.of(page, pageSize);
        return trailService.findAll(pr);
    }

    @GetMapping("/{id}")
    public Trail getTrail(@PathVariable Long id) {
        return trailService.findById(id);
    }
}
