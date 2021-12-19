package com.nmtrails.appcontest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.repositories.TrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrailServiceImpl implements TrailService {

    TrailRepository repo;
    WebClient webclient;

    private String gApiKey;
    private String gEngineId;

    @Autowired
    public TrailServiceImpl(TrailRepository repo, WebClient webclient, Environment env) {
        this.repo = repo;
        this.webclient = webclient;
        this.gApiKey = env.getProperty("google.api_key");
        this.gEngineId = env.getProperty("google.engine_id");
    }

    @Override
    public Trail findById(Long id) {
        Optional<Trail> trail = repo.findById(id);
        if (trail.isPresent()) {
            Trail t = trail.get();
            lazyLoadImageUrl(t);
            return t;
        }
        throw new RuntimeException("Trail not found.");
    }

    @Override
    public List<Trail> findAllByNameLike(String name, Pageable pageable) {
        String nameLike = String.format("%%%s%%", name);
        List<Trail> result = repo.findAllByNameLike(nameLike, pageable).toList();
        for (Trail t : result) {
            lazyLoadImageUrl(t);
        }
        return result;
    }

    @Override
    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    /*
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
        if (link.length() <= 1000) trail.setImageUrl(link);
        trail.setHasImage(true);
        this.repo.save(trail);
    }

    private String getLinkFromJson(String json) {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        try {
            map = om.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Object> items = (List<Object>) map.get("items");
        String link = (String) ((Map<String, Object>) items.get(0)).get("link");
        System.out.println(link);
        return link;
    }
}
