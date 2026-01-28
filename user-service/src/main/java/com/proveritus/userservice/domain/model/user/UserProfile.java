package com.proveritus.userservice.domain.model.user;

import com.proveritus.userservice.shared.domain.id.HibernateIdGeneratorAdapter;
import com.tinash.cloud.utility.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

/**
 * User profile value object.
 */
@Getter
@Embeddable
public class UserProfile extends BaseEntity {
    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(
            name = "ulid-generator",
            type = HibernateIdGeneratorAdapter.class,
            parameters = @Parameter(name = "prefix", value = "USR-")
    )
    private String id;
    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name="email", nullable = false, length = 255)
    private String email;

    protected UserProfile() {
    }

    public UserProfile(String username, String firstName, String lastName, String phoneNumber, String email) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.username = username;
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.phoneNumber = phoneNumber != null ? phoneNumber.trim() : null;
        this.email = email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
