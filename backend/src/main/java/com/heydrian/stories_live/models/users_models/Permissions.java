package com.heydrian.stories_live.models.users_models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Permissions {

    // The primary key is represented by UPPERCASE
    // words that denotes what it does
    // i.e: CREATE_STORY, DELETE_OWN_STORY, UPDATE_OWN_STORY, etc.
    @Id
    @Column(name = "permission_name", nullable = false)
    private String permissionName;

    @Column(name = "permission_description")
    private String permissionDescription;
}
