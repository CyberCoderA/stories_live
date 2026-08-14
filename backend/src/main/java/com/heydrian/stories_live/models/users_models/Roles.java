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
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Roles {
    @Id
    @Column(name = "role_id", nullable = false)
    private String roleId;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;
}
