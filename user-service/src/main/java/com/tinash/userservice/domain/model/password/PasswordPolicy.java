package com.tinash.userservice.domain.model.password;

import com.tinash.userservice.domain.model.IdPrefix;
import com.tinash.userservice.shared.domain.id.HibernateIdGeneratorAdapter;
import com.tinash.cloud.utility.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "password_policy")
@Getter
@Setter
public class PasswordPolicy extends BaseEntity {
    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(
            name = "ulid-generator",
            type = HibernateIdGeneratorAdapter.class,
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = IdPrefix.PASSWORD_POLICY)
    )
    private String id;

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