package com.proveritus.userservice.domain.model.usergroup;

import com.proveritus.cloudutility.jpa.BaseEntity;
import com.proveritus.userservice.domain.model.permission.Permission;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = "permissions")
@Entity
@Table(name = "user_groups")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    public UserGroup(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions = new HashSet<>();
}