package com.heydrian.stories_live.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;

import com.heydrian.stories_live.models.users_models.Users;
import com.heydrian.stories_live.repository.users_repository.UsersRepository;

@Service
public class UserService {
    @Autowired
    private UsersRepository repo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users addUser(Users user) {
        user.setUserPassword(encoder.encode(user.getUserPassword()));
        user.setUserVerificationCode(encoder.encode(user.getUserVerificationCode()));
        return repo.save(user);
    }

    public String verify(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (authentication.isAuthenticated()) {
            Users user = repo.findByUserEmail(email);
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
}
