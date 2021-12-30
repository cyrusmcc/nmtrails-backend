package com.nmtrails.appcontest.controllers;

import com.nmtrails.appcontest.entities.Region;
import com.nmtrails.appcontest.entities.RegionView;
import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.services.RegionService;
import com.nmtrails.appcontest.services.RegionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/regions")
public class RegionController {
    private RegionService service;

    @Autowired
    public RegionController(RegionService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<RegionView> listRegions(@RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageRequest pr = PageRequest.of(page, pageSize);
        return service.findAll(pr);
    }

    @GetMapping("/{id}/trails")
    public Set<Trail> trailsInRegion(@PathVariable Long id) {
        Region region = service.findById(id);
        return region.getTrails();
    }

    @GetMapping("/featured")
    public RegionView featuredRegion() throws FileNotFoundException {
        return service.randomRegion();
    }
}
