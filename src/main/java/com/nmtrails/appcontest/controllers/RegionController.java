package com.nmtrails.appcontest.controllers;

import com.nmtrails.appcontest.entities.Region;
import com.nmtrails.appcontest.entities.RegionView;
import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.payload.requests.TokenRefreshRequest;
import com.nmtrails.appcontest.services.RegionService;
import com.nmtrails.appcontest.services.RegionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

/**
 * API Documentation:
 * @see <a href="https://nmtrails-api-docs.readthedocs.io/en/latest/">here</a>
 * */
@RestController
@RequestMapping("/api/regions")
public class RegionController {
    private RegionService service;

    @Autowired
    public RegionController(RegionService service) {
        this.service = service;
    }

    /**
     * Given page and page size, retrieve appropriate regions
     * * */
    @GetMapping("/")
    public List<RegionView> listRegions(@RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageRequest pr = PageRequest.of(page, pageSize);
        return service.findAll(pr);
    }

    /**
     * Given region ID, find its trails
     * * */
    @GetMapping("/{id}/trails")
    public Set<Trail> trailsInRegion(@PathVariable Long id) {
        Region region = service.findById(id);
        return region.getTrails();
    }

    /**
     * Retrieve random region and return it as featured region
     * * */
    @GetMapping("/featured")
    public RegionView featuredRegion() throws FileNotFoundException {
        return service.randomRegion();
    }
}
