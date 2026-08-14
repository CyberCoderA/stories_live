package com.heydrian.stories_live.models.users_models;

import java.time.Instant;

import com.heydrian.stories_live.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_notifications")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserNotifications {
    
    @Id
    @Column(name = "notification_id", nullable = false)
    private String notificationId;

    // Association with the Users entity.
    // Many notifications can belong to one recipient.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "notification_message", nullable = false)
    private String notificationMessage;

    // This field is used to reference the actor (user) who triggered the notification
    // Nullable since a notif can be system generated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_reference", referencedColumnName = "user_id")
    private Users actor;

    // Nullable if the user hasn't viewed the notification yet
    @Column(name = "notification_is_viewed_at")
    private Instant notificationIsViewedAt;

    @Column(name = "notification_created_at", nullable = false)
    private Instant notificationCreatedAt;
}