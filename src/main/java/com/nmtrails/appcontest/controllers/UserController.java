package com.nmtrails.appcontest.controllers;

import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.entities.User;
import com.nmtrails.appcontest.payload.requests.AddTrailToHikeListRequest;
import com.nmtrails.appcontest.payload.requests.RemoveTrailFromToHikeListRequest;
import com.nmtrails.appcontest.payload.responses.MessageResponse;
import com.nmtrails.appcontest.services.TrailService;
import com.nmtrails.appcontest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    private TrailService trailService;

    @Autowired
    public UserController(UserService userService, TrailService trailService) {
        this.userService = userService;
        this.trailService = trailService;
    }

    /**
     * Given id, check if valid and return user object
     * */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        if (!userService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User does not exist"));
        }

        return ResponseEntity.ok(userService.findById(id).getUsername());

    }

    /**
     * Given user ID and list type (to-hike or finished hikes list), return tarils
     * belonging to the user's list
     * */
    @GetMapping("/user-trail-list")
    public ResponseEntity<?> getUserTrailList(@RequestParam Long userId,
                                              @RequestParam String listType) {

        if (!userService.existsById(userId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User does not exist"));
        }

        if (listType.equals("To Hike"))
            return ResponseEntity.ok(userService.findById(userId).getToHikeList());


        if (listType.equals("Hiked"))
            return ResponseEntity.ok(userService.findById(userId).getHikedList());


        else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Invalid request"));
        }

    }

    /**
     * Given id, check if valid and return user object
     * */
    @GetMapping("/has-trail-in-to-hike-list")
    public ResponseEntity<?> hasTrailInToHikeList(@RequestParam Long userId,
                                                @RequestParam Long trailId) {

        if (!userService.existsById(userId) || !trailService.existsById(trailId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error encountered while adding trail to hike list"));
        }

        return ResponseEntity.ok(userService.findById(userId).hasTrailInToHikeList(trailService.findById(trailId)));
    }

    /**
     * When a request to add a trail to user's hike list is made, verify that user and trail id are valid,
     * and that user does not have trail in list already, then add to list.
     * */
    @PostMapping("/add-trail-to-hike-list")
    public ResponseEntity<?> addTrailToHikeList(@RequestBody AddTrailToHikeListRequest request) {

        if (!userService.existsById(request.getUserId()) || !trailService.existsById(request.getTrailId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error encountered while adding trail to hike list"));
        }

        if (request.getListType().equals("To Hike")) {

            User user = userService.findById(request.getUserId());
            user.addTrailToHikeList(trailService.findById(request.getTrailId()));
            userService.save(user);

            return ResponseEntity.ok(new MessageResponse("Trail added to hike list"));
        }

        if (request.getListType().equals("Hiked")) {

            User user = userService.findById(request.getUserId());
            Trail trail = trailService.findById(request.getTrailId());

            user.addTrailToHikedList(trail);
            userService.save(user);

            if (request.getUserRating() > 0) {
                trail.setRatings(trail.getRatings() + 1);
                trail.setSumOfRatings(trail.getSumOfRatings() + request.getUserRating());
                trail.setAvgRating();
                trailService.save(trail);
            }

            return ResponseEntity.ok(new MessageResponse("Trail added to hiked list"));
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error encountered while adding to list"));
    }

    /**
     * When a request to remove a trail to user's hike list is made, verify that user and trail id are valid,
     * and that user has trail in list, then remove to list.
     * */
    @PostMapping("/remove-trail-from-user-list")
    public ResponseEntity<?> removeTrailFromUserList(@RequestBody RemoveTrailFromToHikeListRequest request) {

        if (!userService.existsById(request.getUserId()) || !trailService.existsById(request.getTrailId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error encountered while adding trail to hike list"));
        }

        User user = userService.findById(request.getUserId());

        if (request.getListType().equals("To Hike")) {
            user.removeTrailFromToHikeList(trailService.findById(request.getTrailId()));
        }

        else if (request.getListType().equals("Hiked"))
            user.removeTrailFromHikedList(trailService.findById(request.getTrailId()));

        else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Invalid request"));
        }

        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("Trail removed from list"));
    }
}
