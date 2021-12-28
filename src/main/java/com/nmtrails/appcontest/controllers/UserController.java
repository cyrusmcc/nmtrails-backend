package com.nmtrails.appcontest.controllers;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        if (!userService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User does not exist"));
        }

        return ResponseEntity.ok(userService.findById(id).getUsername());

    }

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

    @PostMapping("/add-trail-to-hike-list")
    public ResponseEntity<?> addTrailToHikeList(@RequestBody AddTrailToHikeListRequest request) {

        System.out.println(request.getListType());

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
            user.addTrailToHikedList(trailService.findById(request.getTrailId()));
            userService.save(user);

            return ResponseEntity.ok(new MessageResponse("Trail added to hiked list"));
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error encountered while adding to list"));
    }

    @PostMapping("/remove-trail-from-user-list")
    public ResponseEntity<?> removeTrailFromUserList(@RequestBody RemoveTrailFromToHikeListRequest request) {

        System.out.println("1" + request.getListType());

        if (!userService.existsById(request.getUserId()) || !trailService.existsById(request.getTrailId())) {
            System.out.println("2");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error encountered while adding trail to hike list"));
        }

        User user = userService.findById(request.getUserId());

        if (request.getListType().equals("To Hike")) {
            user.removeTrailFromToHikeList(trailService.findById(request.getTrailId()));
            System.out.println("3");
        }

        else if (request.getListType().equals("Hiked"))
            user.removeTrailFromHikedList(trailService.findById(request.getTrailId()));

        else {
            System.out.println("5");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Invalid request"));
        }

        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("Trail removed from list"));
    }
}
