package com.nmtrails.appcontest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.repositories.SegmentRepository;
import com.nmtrails.appcontest.repositories.TrailRepository;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TrailServiceImpl implements TrailService {

    TrailRepository trailRepository;
    SegmentRepository segmentRepository;
    WebClient webclient;
    GeometryFactory factory;

    private String gApiKey;
    private String gEngineId;

    @Autowired
    public TrailServiceImpl(TrailRepository trailRepository,
                            SegmentRepository segmentRepository,
                            WebClient webclient,
                            Environment env) {
        this.trailRepository = trailRepository;
        this.segmentRepository = segmentRepository;
        this.webclient = webclient;
        this.factory = new GeometryFactory();
        this.gApiKey = env.getProperty("google.api_key");
        this.gEngineId = env.getProperty("google.engine_id");
    }


    /**
     * Return region given a region id
     * */
    @Override
    public Trail findById(Long id) {
        Optional<Trail> trail = trailRepository.findById(id);
        if (trail.isPresent()) {
            Trail t = trail.get();
            //lazyLoadImageUrl(t);
            return t;
        }
        throw new RuntimeException("Trail not found.");
    }

    /**
     * Return all region entries given {@link Pageable} object. Optionally,
     * if user provides a search term, return matching trails
     * */
    @Override
    public List<Trail> findAllByNameLike(String name, Pageable pageable) {
        String nameLike = String.format("%%%s%%", name);
        List<Trail> result = trailRepository.findAllByNameLikeIgnoreCase(nameLike, pageable).toList();
        for (Trail t : result) {
            //lazyLoadImageUrl(t);
        }
        return result;
    }

    /**
     * Given a list of ids, return their bounding box
     * * */
    @Override
    public Geometry findExtent(List<Long> ids) {

        if (ids == null || ids.size() == 0) throw new IllegalArgumentException();

        WKTReader reader = new WKTReader(factory);
        try {
            return reader.read(segmentRepository.findTrailsExtent(ids));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Could not parse geometry");
    }

    @Override
    public boolean existsById(Long id) {
        return trailRepository.existsById(id);
    }

    /**
    If the trail doesn't have an image yet, attempt to fetch one from the
    Google search API.
     */
    private void lazyLoadImageUrl(Trail trail) {
        if (trail.hasImage()) return;

        String clientResponse = webclient.get()
                .uri((uriBuilder -> uriBuilder.path("/")
                        .queryParam("key", gApiKey)
                        .queryParam("cx", gEngineId)
                        .queryParam("searchType", "image")
                        .queryParam("q", trail.getName())
                        .build()))
                .exchangeToMono(response -> {
                    if (response.statusCode()
                            .equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode()
                            .is4xxClientError()) {
                        return Mono.just("Error response");
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                }).block();

        String link = getLinkFromJson(clientResponse);
        if (link != null && link.length() <= 1000) trail.setImageUrl(link);
        trail.setHasImage(true);
        this.trailRepository.save(trail);
    }

    private String getLinkFromJson(String json) {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        try {
            map = om.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        // if the request resulted in an error, don't bother loading an image url
        if (map.containsKey("error")) return null;

        List<Object> items = (List<Object>) map.get("items");
        String link = (String) ((Map<String, Object>) items.get(0)).get("link");
        return link;
    }

    public void save(Trail trail) {
        trailRepository.save(trail);
    }

    /**
     * Create and return {@link Pageable} object given page number and size containing trails
     * sorted by total number of ratings.
     * * */
    @Override
    public List<Trail> findAllByRatingsDesc(Integer pageNum, Integer pageSize) {

        Pageable paging = PageRequest.of(pageNum, pageSize, Sort.by("ratings").descending());

        Slice<Trail> sliceResult = trailRepository.findAll(paging);

        if (sliceResult.hasContent()) {
            return sliceResult.getContent();
        } else return new ArrayList<>();

    }

    /**
     * Return list of featured trails provided by a text file. If fnfe,
     * generate and return a random list of trails contained in first 10 dB entries
     * for trails.
     * * */
    @Override
    public List<Trail> getFeaturedTrails() {

        List<Trail> trails = new ArrayList<>();

        try {

            Scanner scanner = new Scanner(new File("componentdata/featuredtrails.txt"));
            while (scanner.hasNextLine()) {
                trails.add(trailRepository.findByName(scanner.nextLine()));
            }

            return trails;

         // fallback if fnfe
        } catch (FileNotFoundException e) {

            trails = new ArrayList<>();

            for (int i = 0; i < 3; i++) {

                long randomId = ThreadLocalRandom.current().nextLong(10);
                Trail t = trailRepository.findById(randomId).get();

                while (trails.contains(t)) {
                    randomId = ThreadLocalRandom.current().nextLong(10);
                    if (trailRepository.existsById(randomId))
                        t = trailRepository.findById(randomId).get();
                    else continue;
                }
                trails.add(t);
            }
            return trails;
        }
    }
}
