package com.heydrian.stories_live.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heydrian.stories_live.dto.request.LoginRequest;
import com.heydrian.stories_live.dto.request.RegisterRequest;
import com.heydrian.stories_live.dto.request.VerifyEmailRequest;
import com.heydrian.stories_live.dto.response.ApiResponse;
import com.heydrian.stories_live.dto.response.UserResponse;
import com.heydrian.stories_live.enums.UserStatus;
import com.heydrian.stories_live.exception.ErrorResponse;
import com.heydrian.stories_live.models.users_models.Users;
import com.heydrian.stories_live.repository.users_repository.UsersRepository;
import com.heydrian.stories_live.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private Random rnd = new Random();
    private final UsersRepository usersRepository;
    private final UserService userService;
    
    // Constructor for UsersController that takes a UsersRepository as a parameter
    public UsersController(UsersRepository usersRepository, UserService userService) {
        this.usersRepository = usersRepository;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.verify(request.email(), request.password());

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(
                    HttpStatus.UNAUTHORIZED.value(),
                    "INVALID_CREDENTIALS",
                    "Email or password is incorrect"
                ));
        }

        // Code block to embed the JWT token into the browser
        // ResponseCookie cookie = ResponseCookie.from("jwt", token)
        //         .httpOnly(true)
        //         .path("/")
        //         .maxAge(1L * 60 * 60)
        //         .build();

        // response.addHeader("Set-Cookie", cookie.toString());

        // Log the generated token
        // TO BE REMOVED LATER!!! USED ONLY FOR POSTMAN TESTING
        System.out.println("Generated [JWT] Token: " + token); 
        return ResponseEntity.ok(
            ApiResponse.of(
                HttpStatus.OK.value(),
                "Login successful",
                Map.of("token", token)
            )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @ModelAttribute RegisterRequest request) {
        // Validate if the email already exists in the database
        if (usersRepository.findByUserEmail(request.email()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(
                    HttpStatus.CONFLICT.value(),
                    "DUPLICATE_RESOURCE",
                    "Email already exists"
                ));
        }

        Instant currentTimestamp = Instant.now();
        Users user = new Users(
            String.format("%s-%s", request.username(), UUID.randomUUID().toString()),
            request.username(),
            request.password(),
            request.email(),
            UserStatus.PENDING_VERIFICATION,
            currentTimestamp,
            currentTimestamp,
            String.format("%06d", rnd.nextInt(100000)),
            currentTimestamp.plus(Duration.ofMinutes(1)),
            false
        );

        userService.addUser(user);

        return new ResponseEntity<>(
            ApiResponse.of(
                HttpStatus.CREATED.value(),
                "User registered successfully",
                UserResponse.fromUser(user)
            ),
            HttpStatus.CREATED
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        return ResponseEntity.ok("Testing");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        Users user = usersRepository.findByUserEmail(authentication.getName());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                    HttpStatus.NOT_FOUND.value(),
                    "RESOURCE_NOT_FOUND",
                    "User not found"
                ));
        }

        return ResponseEntity.ok(
            ApiResponse.of(
                HttpStatus.OK.value(),
                "User retrieved successfully",
                UserResponse.fromUser(user)
            )
        );
    }

}
