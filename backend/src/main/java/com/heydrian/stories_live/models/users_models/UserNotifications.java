package com.heydrian.stories_live.models.users_models;

import com.heydrian.stories_live.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

public class UserNotifications {
    
    @Id
    @Column(name = "notification_id", nullable = false)
    private String notificationId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    // Association with the Users entity, establishing a one-to-one relationship
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    
}
