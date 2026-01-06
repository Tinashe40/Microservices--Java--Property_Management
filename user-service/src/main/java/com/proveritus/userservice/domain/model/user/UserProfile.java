package com.proveritus.userservice.domain.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * User profile value object.
 */
@Embeddable
public class UserProfile {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    protected UserProfile() {
    }

    public UserProfile(String firstName, String lastName, String phoneNumber) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.phoneNumber = phoneNumber != null ? phoneNumber.trim() : null;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
