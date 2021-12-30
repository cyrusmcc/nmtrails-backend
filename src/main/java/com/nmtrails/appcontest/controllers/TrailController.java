package com.nmtrails.appcontest.controllers;

import com.nmtrails.appcontest.entities.Segment;
import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.payload.responses.MessageResponse;
import com.nmtrails.appcontest.services.TrailService;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/trails")
public class TrailController {
    private TrailService trailService;

    @Autowired
    public TrailController(TrailService trailService) {
        this.trailService = trailService;
    }

    @GetMapping("/")
    public List<Trail> listTrails(@RequestParam(required = false, defaultValue = "") String name,
                                  @RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageRequest pr = PageRequest.of(page, pageSize);
        if (name == null) name = "";

        return trailService.findAllByNameLike(name, pr);
    }

    @GetMapping("/{id}")
    public Trail getTrail(@PathVariable Long id) {
        return trailService.findById(id);
    }

    @GetMapping("/{id}/segments")
    public Set<Segment> getTrailSegments(@PathVariable Long id) {
        return trailService.findById(id).getSegments();
    }

    @GetMapping("/extent")
    public ResponseEntity<?> getExtent(@RequestParam(defaultValue = "") List<Long> ids) {

        if (ids == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error encountered while retrieving map data"));
        }

        return ResponseEntity.ok(trailService.findExtent(ids));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularTrails(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "5") int pageSize) {

        List<Trail> list = trailService.findAllByRatingsDesc(page, pageSize);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedTrails() {

        return ResponseEntity.ok(trailService.getRandomTrails(3));
    }



}
