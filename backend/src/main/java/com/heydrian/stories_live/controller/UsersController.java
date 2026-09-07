package com.heydrian.stories_live.controller;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heydrian.stories_live.dto.RegisterRequest;
import com.heydrian.stories_live.enums.UserStatus;
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

        // Code block to embed the JWT token into the browser
        // ResponseCookie cookie = ResponseCookie.from("jwt", token)
        //         .httpOnly(true)
        //         .path("/")
        //         .maxAge(1L * 60 * 60)
        //         .build();

        // response.addHeader("Set-Cookie", cookie.toString());

        System.out.println("Generated [JWT] Token: " + token); // Log the generated token
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @ModelAttribute RegisterRequest request) {
        if (usersRepository.findByUserEmail(request.email()) != null) {
            return new ResponseEntity<>(Map.of("message", "Email already exists"), HttpStatus.BAD_REQUEST);
        }

        Instant currentTimestamp = Instant.now();
        Users user = new Users(
            String.format("%s-%s", request.username(), UUID.randomUUID().toString()),
            request.username(),
            request.password(),
            request.email(),
            UserStatus.ACTIVE,
            currentTimestamp,
            currentTimestamp
        );

        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
