package com.tinash.userservice.domain.model.password;

import com.tinash.cloud.utility.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "password_history")
@Getter
@Setter
public class PasswordHistory extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;
}