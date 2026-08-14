package com.heydrian.stories_live.models.embeded;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Represents the composite primary key for the RolePermission entity,
// which serves as the association between Roles and Permissions.

// The overall relationship between Roles and Permissions is many-to-many,
// with RolePermissions acting as the association entity.
@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode // used for verifying equality of composite keys
public class RolePermissionId implements Serializable {
    
    @Column(name = "role_id", nullable = false)
    private String roleId;

    @Column(name = "permission_name", nullable = false)
    private String permissionName;
}
