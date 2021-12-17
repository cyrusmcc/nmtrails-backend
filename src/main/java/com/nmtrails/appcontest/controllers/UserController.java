package com.nmtrails.appcontest.controllers;

import com.nmtrails.appcontest.payload.responses.MessageResponse;
import com.nmtrails.appcontest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/wishlist/{id}")
    public ResponseEntity<?> getUserWishList(@PathVariable Long id) {

        if (!userService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User does not exist"));
        }

        return ResponseEntity.ok(userService.findById(id).getWishList());
    }

    @GetMapping("/hikedList/{id}")
    public ResponseEntity<?> getUserHikedTrails(@PathVariable Long id) {

        if (!userService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User does not exist"));
        }

        return ResponseEntity.ok(userService.findById(id).getHikedList());
    }

}
