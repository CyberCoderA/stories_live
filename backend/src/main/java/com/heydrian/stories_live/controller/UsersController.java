package com.heydrian.stories_live.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heydrian.stories_live.dto.request.ForgotPasswordRequest;
import com.heydrian.stories_live.dto.request.LoginRequest;
import com.heydrian.stories_live.dto.request.PasswordResetRequest;
import com.heydrian.stories_live.dto.request.RegisterRequest;
import com.heydrian.stories_live.dto.request.ResendVerificationRequest;
import com.heydrian.stories_live.dto.request.UpdateUsernameRequest;
import com.heydrian.stories_live.dto.request.VerifyEmailRequest;
import com.heydrian.stories_live.dto.response.UserResponse;
import com.heydrian.stories_live.enums.UserStatus;
import com.heydrian.stories_live.models.users_models.Users;
import com.heydrian.stories_live.repository.users_repository.UsersRepository;
import com.heydrian.stories_live.response.ResponseHandler;
import com.heydrian.stories_live.services.EmailService;
import com.heydrian.stories_live.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersRepository usersRepository;
    private final UserService userService;

    @Autowired 
    private EmailService emailService;
    
    // Constructor for UsersController that takes a UsersRepository as a parameter
    public UsersController(UsersRepository usersRepository, UserService userService) {
        this.usersRepository = usersRepository;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.verify(request.email(), request.password());

        if (token == null) {
            return ResponseHandler.error(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Email or password is incorrect");
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
        return ResponseHandler.success(HttpStatus.OK, "Login successful", Map.of("token", token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.requestPasswordReset(request.email());

        return ResponseHandler.success(
            HttpStatus.OK,
            "A password reset email has been sent to: " + request.email(),
            Map.of("email", request.email())
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        boolean resetSuccessful = userService.resetPassword(
            request.selector(),
            request.token(),
            request.newPassword()
        );

        if (!resetSuccessful) {
            return ResponseHandler.error(HttpStatus.BAD_REQUEST, "INVALID_RESET_TOKEN", "The reset token is invalid or expired");
        }

        return ResponseHandler.success(HttpStatus.OK, "Password reset successfully", Map.of("status", "updated"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @ModelAttribute RegisterRequest request) {
        // Validate if the email already exists in the database
        if (usersRepository.findByUserEmail(request.email()) != null) {
            return ResponseHandler.error(HttpStatus.CONFLICT, "DUPLICATE_RESOURCE", "Email already exists");
        }

        Instant currentTimestamp = Instant.now();
        String verificationCode = emailService.generateOtp();
        Users user = new Users(
            String.format("%s-%s", request.username(), UUID.randomUUID().toString()),
            request.username(),
            request.password(),
            request.email(),
            UserStatus.PENDING_VERIFICATION,
            currentTimestamp,
            currentTimestamp,
            verificationCode,
            currentTimestamp.plus(Duration.ofMinutes(1)),
            false
        );
        userService.addUser(user);

        emailService.sendVerificationEmail(request.email(), verificationCode);

        return ResponseHandler.created("User registered successfully", UserResponse.fromUser(user));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        if (!userService.verifyEmail(request.email(), request.verificationCode())) {
            return ResponseHandler.error(HttpStatus.BAD_REQUEST, "INVALID_VERIFICATION_CODE", "The verification code is invalid or expired");
        }

        return ResponseHandler.success(HttpStatus.OK, "Email verified successfully", Map.of("email", request.email()));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        if (!userService.resendVerification(request.email())) {
            return ResponseHandler.error(HttpStatus.BAD_REQUEST, "INVALID_VERIFICATION_REQUEST", "The account cannot receive a verification email");
        }

        return ResponseHandler.success(HttpStatus.OK, "Verification email sent successfully", Map.of("email", request.email()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        Users user = usersRepository.findByUserEmail(authentication.getName());

        if (user == null) {
            return ResponseHandler.error(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "User not found");
        }

        return ResponseHandler.success(HttpStatus.OK, "User retrieved successfully!", UserResponse.fromUser(user));
    }

    @PostMapping("/update-username")
    public ResponseEntity<?> updateUsername(Authentication authentication,
        @Valid @RequestBody UpdateUsernameRequest request) {

        if (authentication == null || authentication.getName() == null) {
            return ResponseHandler.error(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication is required");
        }

        try {
            Users user = userService.updateUsername(authentication.getName(), request.newUsername());
            return ResponseHandler.success(HttpStatus.OK, "Username successfully changed!", Map.of("username", user.getUsername()));
        } catch (IllegalArgumentException ex) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            String code = "INVALID_USERNAME";

            if ("Username already exists".equals(ex.getMessage())) {
                status = HttpStatus.CONFLICT;
                code = "DUPLICATE_RESOURCE";
            } else if ("User not found".equals(ex.getMessage())) {
                status = HttpStatus.NOT_FOUND;
                code = "RESOURCE_NOT_FOUND";
            }

            return ResponseHandler.error(status, code, ex.getMessage());
        }
    }

}
