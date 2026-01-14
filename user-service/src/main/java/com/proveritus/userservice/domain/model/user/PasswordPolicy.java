package com.proveritus.userservice.domain.model.user;

import com.proveritus.cloudutility.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "password_policy")
@Getter
@Setter
public class PasswordPolicy extends BaseEntity {

    @Column(name = "min_length", nullable = false)
    private int minLength = 8;

    @Column(name = "max_length", nullable = false)
    private int maxLength = 64;

    @Column(name = "requires_uppercase", nullable = false)
    private boolean requiresUppercase = true;

    @Column(name = "requires_lowercase", nullable = false)
    private boolean requiresLowercase = true;

    @Column(name = "requires_number", nullable = false)
    private boolean requiresNumber = true;

    @Column(name = "requires_special_char", nullable = false)
    private boolean requiresSpecialChar = true;

    @Column(name = "password_history_count", nullable = false)
    private int passwordHistoryCount = 5;

    @Column(name = "password_expiration_days", nullable = false)
    private int passwordExpirationDays = 90;
}