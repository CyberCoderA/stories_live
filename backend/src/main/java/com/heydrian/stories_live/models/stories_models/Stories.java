package com.heydrian.stories_live.models.stories_models;

import jakarta.persistence.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.heydrian.stories_live.enums.UserStatus;

@Entity 
@Table(name = "stories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stories {

    @Id 
    @Column(name = "story_id", nullable = false)
    private String storyId;
}
