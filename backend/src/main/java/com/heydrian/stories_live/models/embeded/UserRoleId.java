package com.heydrian.stories_live.models.embeded;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserRoleId implements Serializable{

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "role_id", nullable = false)
    private String roleId;
}
