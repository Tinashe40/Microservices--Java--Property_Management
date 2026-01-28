package com.proveritus.userservice.domain.model.user;

import com.tinash.cloud.utility.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "user_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToken extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(
            name = "ulid-generator",
            type = com.proveritus.userservice.shared.domain.id.HibernateIdGeneratorAdapter.class,
            parameters = @Parameter(name = "prefix", value = "UT-")
    )
    private String id;

    @Column(nullable = false, unique = true, columnDefinition = "LONGTEXT")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private boolean blacklisted = false;
}