package com.heydrian.stories_live.dto.response;

import java.time.Instant;
import com.heydrian.stories_live.enums.UserStatus;
import com.heydrian.stories_live.models.users_models.Users;

public record UserResponse(
    String userId,
    String username,
    String userEmail,
    UserStatus userStatus,
    Instant createdAt,
    Instant lastLoginAt
) {
    public static UserResponse fromUser(Users user) {
        return new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getUserEmail(),
            user.getUserStatus(),
            user.getCreatedAt(),
            user.getLastLoginAt()
        );
    }
}