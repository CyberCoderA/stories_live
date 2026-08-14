package com.heydrian.stories_live.models.users_models;

import com.heydrian.stories_live.models.embeded.RolePermissionId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Represents the association between Roles and Permissions.
// Each RolePermissions record references one Role and one Permission.
// The overall relationship between Roles and Permissions is many-to-many.
@Entity
@Table(name = "role_permissions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissions {
    
    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId") // tells JPA to use the same primary key value as the associated User entity
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionName")
    @JoinColumn(name = "permission_name", nullable = false)
    private Permissions permission;

}
