package com.heydrian.stories_live.repository.users_repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heydrian.stories_live.models.users_models.PasswordResetToken;
import com.heydrian.stories_live.models.users_models.Users;

public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findBySelector(String selector);
    void deleteByUser(Users user);
}
