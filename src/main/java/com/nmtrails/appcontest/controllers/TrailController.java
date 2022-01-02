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

/**
 * API Documentation:
 * @see <a href="https://nmtrails-api-docs.readthedocs.io/en/latest/">here</a>
 * */
@RestController
@RequestMapping("/api/trails")
public class TrailController {
    private TrailService trailService;

    @Autowired
    public TrailController(TrailService trailService) {
        this.trailService = trailService;
    }

    /**
     * Given page and page size, retrieve appropriate trails. Optionally, if a user wants to search
     * for specific trails and provides a name and return the results.
     * * */
    @GetMapping("/")
    public List<Trail> listTrails(@RequestParam(required = false, defaultValue = "") String name,
                                  @RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageRequest pr = PageRequest.of(page, pageSize);
        if (name == null) name = "";

        return trailService.findAllByNameLike(name, pr);
    }

    /**
     * Find trail by specified id
     * * */
    @GetMapping("/{id}")
    public Trail getTrail(@PathVariable Long id) {
        return trailService.findById(id);
    }


    /**
     * Given a trail's id, retrieve its coordinate segments for map population
     * * */
    @GetMapping("/{id}/segments")
    public Set<Segment> getTrailSegments(@PathVariable Long id) {
        return trailService.findById(id).getSegments();
    }

    /**
     * Given a list of ids, return their bounding box
     * * */
    @GetMapping("/extent")
    public ResponseEntity<?> getExtent(@RequestParam(defaultValue = "") List<Long> ids) {

        if (ids == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error encountered while retrieving map data"));
        }

        return ResponseEntity.ok(trailService.findExtent(ids));
    }

    /**
     * Given page and page size, return a list of popular trails which are sorted by total number of ratings
     * * */
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularTrails(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "5") int pageSize) {

        List<Trail> list = trailService.findAllByRatingsDesc(page, pageSize);

        return ResponseEntity.ok(list);
    }

    /**
     * Retrieve random trails and return as featured trails
     * * */
    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedTrails() {

        return ResponseEntity.ok(trailService.getFeaturedTrails());
    }



}
