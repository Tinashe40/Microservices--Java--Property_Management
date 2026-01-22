package com.tinash.cloud.utility.password;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a historical password entry for a user.
 * This entity is used to enforce password reuse policies.
 */
@Entity
@Table(name = "password_history")
@Getter
@Setter
public class PasswordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // The ID of the user this password history belongs to

    @Column(nullable = false)
    private String hashedPassword; // The hashed password

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
