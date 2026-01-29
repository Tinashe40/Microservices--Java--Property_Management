package com.tinash.userservice.domain.model.password;

import com.tinash.userservice.domain.model.IdPrefix;
import com.tinash.userservice.shared.domain.id.HibernateIdGeneratorAdapter;
import com.tinash.cloud.utility.jpa.BaseEntity;
import com.tinash.userservice.domain.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.Instant;

@Entity
@Table(name = "password_reset_tokens")
@Data
@NoArgsConstructor
public class PasswordResetToken extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(
            name = "ulid-generator",
            type = HibernateIdGeneratorAdapter.class,
            parameters = @Parameter(name = "prefix", value = IdPrefix.PASSWORD_RESET_TOKEN)
    )
    private String id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    public PasswordResetToken(String token, User user, Instant expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }
}
