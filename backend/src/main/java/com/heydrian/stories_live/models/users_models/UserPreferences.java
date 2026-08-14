package com.heydrian.stories_live.models.users_models;

import com.heydrian.stories_live.enums.Theme;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferences {
    
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    // Denotes the association with the User entity, establishing a one-to-one relationship
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // tells JPA to use the same primary key value as the associated User entity
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_theme", nullable = false)
    private Theme preferredTheme;

    @Column(name = "preferred_display_font_size", nullable = false)
    private Integer preferredDisplayFontSize;
}
