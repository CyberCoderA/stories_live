package com.heydrian.stories_live.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import com.heydrian.stories_live.models.users_models.PasswordResetToken;
import com.heydrian.stories_live.models.users_models.Users;
import com.heydrian.stories_live.repository.users_repository.PasswordResetRepository;
import com.heydrian.stories_live.repository.users_repository.UsersRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UsersRepository repo;

    @Autowired 
    private PasswordResetRepository resetTokenRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String createVerificationCode(Users user) {
        String verificationCode = emailService.generateOtp();
        user.setUserVerificationCode(encoder.encode(verificationCode));
        user.setVerificationExpiry(Instant.now().plusSeconds(60));
        repo.save(user);
        return verificationCode;
    }

    public boolean resendVerification(String email) {
        Users user = repo.findByUserEmail(email);
        if (user == null || Boolean.TRUE.equals(user.getEmailVerified())) {
            return false;
        }

        String verificationCode = createVerificationCode(user);
        emailService.sendVerificationEmail(user.getUserEmail(), verificationCode);
        return true;
    }

    public Users addUser(Users user) {
        user.setUserPassword(encoder.encode(user.getUserPassword()));
        user.setUserVerificationCode(encoder.encode(user.getUserVerificationCode()));
        return repo.save(user);
    }

    public String verify(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (authentication.isAuthenticated()) {
            Users user = repo.findByUserEmail(email);
            user.setLastLoginAt(Instant.now());
            repo.save(user);

            System.out.println("[LOGIN] " + user.getLastLoginAt());
            if (user != null) {
                return jwtService.generateToken(user);
            }
        }

        return null;
    }

    public boolean verifyEmail(String email, String verificationCode) {
        Users user = repo.findByUserEmail(email);

        if (user == null || Boolean.TRUE.equals(user.getEmailVerified())
                || user.getVerificationExpiry() == null
                || user.getVerificationExpiry().isBefore(Instant.now())
                || !encoder.matches(verificationCode, user.getUserVerificationCode())) {
            return false;
        }

        user.setEmailVerified(true);
        user.setUserStatus(com.heydrian.stories_live.enums.UserStatus.ACTIVE);
        user.setUserVerificationCode(null);
        user.setVerificationExpiry(null);
        repo.save(user);
        return true;
    }

    private String randomToken(int byteCount) {
        byte[] bytes = new byte[byteCount];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes);
    }

    @Transactional
    public void requestPasswordReset(String email) {
        Users user = repo.findByUserEmail(email);

        if (user == null || !Boolean.TRUE.equals(user.getEmailVerified())) {
            return;
        }

        resetTokenRepository.deleteByUser(user);

        String selector = randomToken(16);
        String secret = randomToken(32);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setSelector(selector);
        resetToken.setTokenHash(encoder.encode(secret));
        resetToken.setUser(user);
        resetToken.setExpiresAt(Instant.now().plus(Duration.ofMinutes(15)));

        resetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(
            user.getUserEmail(),
            selector,
            secret
        );
    }

    @Transactional
    public boolean resetPassword(String selector, String token, String newPassword) {
        if (selector == null || token == null || newPassword == null) {
            return false;
        }

        PasswordResetToken resetToken = resetTokenRepository.findBySelector(selector)
            .orElse(null);

        if (resetToken == null || resetToken.isUsed() || resetToken.isExpired()) {
            return false;
        }

        if (!encoder.matches(token, resetToken.getTokenHash())) {
            return false;
        }

        Users user = resetToken.getUser();
        user.setUserPassword(encoder.encode(newPassword));
        repo.save(user);

        resetToken.setUsedAt(Instant.now());
        resetTokenRepository.save(resetToken);
        return true;
    }
}
