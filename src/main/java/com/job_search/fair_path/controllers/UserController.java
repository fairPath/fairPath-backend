package com.job_search.fair_path.controllers;

import com.job_search.fair_path.dataTransferObject.UserProfileDTO;
import com.job_search.fair_path.entity.User;
import com.job_search.fair_path.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }
        try {
            User user = ((User) authentication.getPrincipal());
            UserProfileDTO userProfile = userService.getUserProfile(user.getId());
            System.out.println("User Profile: " + userProfile);
            return ResponseEntity.ok(userProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }

    }

}
