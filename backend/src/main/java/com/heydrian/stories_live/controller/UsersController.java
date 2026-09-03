package com.heydrian.stories_live.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heydrian.stories_live.models.users_models.Users;
import com.heydrian.stories_live.repository.users_repository.UsersRepository;
import com.heydrian.stories_live.services.JWTService;
import com.heydrian.stories_live.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersRepository usersRepository;
    private final UserService userService;
    private final JWTService jwtService;
    
    // Constructor for UsersController that takes a UsersRepository as a parameter
    public UsersController(UsersRepository usersRepository, UserService userService, JWTService jwtService) {
        this.usersRepository = usersRepository;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String userEmail, @RequestParam String userPassword) {
        String token = userService.verify(userEmail, userPassword);

        if (token == null) {
            return new ResponseEntity<>(Map.of("message", "Invalid credentials"), HttpStatus.UNAUTHORIZED);
        }

        System.out.println("Generated JWT Token: " + token); // Log the generated token
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }

}
