package com.heydrian.stories_live.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
