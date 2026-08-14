package com.heydrian.stories_live.models.users_models;

import org.apache.catalina.User;

import com.heydrian.stories_live.enums.Theme;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

public class UserPreferences {
    
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    // Denotes the association with the User entity, establishing a one-to-one relationship
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // tells JPA to use the same primary key value as the associated User entity
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_theme", nullable = false)
    private Theme preferredTheme;

    @Column(name = "preferred_display_font_size", nullable = false)
    private Integer preferredDisplayFontSize;
}
