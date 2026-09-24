package com.heydrian.stories_live.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.heydrian.stories_live.models.users_models.Users;
import com.heydrian.stories_live.repository.users_repository.PasswordResetRepository;
import com.heydrian.stories_live.repository.users_repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordResetRepository passwordResetRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        ReflectionTestUtils.setField(userService, "repo", usersRepository);
        ReflectionTestUtils.setField(userService, "resetTokenRepository", passwordResetRepository);
        ReflectionTestUtils.setField(userService, "jwtService", jwtService);
        ReflectionTestUtils.setField(userService, "authenticationManager", authenticationManager);
        ReflectionTestUtils.setField(userService, "emailService", emailService);
    }

    @Test
    void updateUsername_shouldRejectDuplicateUsername() {
        Users currentUser = new Users();
        currentUser.setUserId("u-1");
        currentUser.setUsername("alice");
        currentUser.setUserEmail("alice@example.com");

        Users duplicateUser = new Users();
        duplicateUser.setUserId("u-2");
        duplicateUser.setUsername("bob");
        duplicateUser.setUserEmail("bob@example.com");

        when(usersRepository.findByUserEmail("alice@example.com")).thenReturn(currentUser);
        when(usersRepository.findByUsername("bob")).thenReturn(duplicateUser);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.updateUsername("alice@example.com", "bob")
        );

        assertEquals("Username already exists", exception.getMessage());
    }
}
