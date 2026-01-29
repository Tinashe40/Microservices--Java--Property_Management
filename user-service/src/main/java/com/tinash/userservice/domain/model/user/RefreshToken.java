package com.tinash.userservice.domain.model.user;

import com.tinash.userservice.domain.model.IdPrefix;
import com.tinash.userservice.shared.domain.id.HibernateIdGeneratorAdapter;
import com.tinash.cloud.utility.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(
            name = "ulid-generator",
            type = HibernateIdGeneratorAdapter.class,
            parameters = @Parameter(name = "prefix", value = "RT-")
    )
    private String id;

    @Column(nullable = false, unique = true, columnDefinition = "LONGTEXT")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;
}
