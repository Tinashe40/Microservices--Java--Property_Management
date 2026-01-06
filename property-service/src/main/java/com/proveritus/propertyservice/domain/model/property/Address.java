package com.proveritus.propertyservice.domain.model.property;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Address value object.
 */
@Embeddable
public class Address {
    
    @Column(name = "address", nullable = false, length = 500)
    private String fullAddress;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 30)
    private String state;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    protected Address() {
    }

    public Address(String fullAddress, String city, String state, String postalCode) {
        if (fullAddress == null || fullAddress.isBlank()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        this.fullAddress = fullAddress.trim();
        this.city = city != null ? city.trim() : null;
        this.state = state != null ? state.trim() : null;
        this.postalCode = postalCode != null ? postalCode.trim() : null;
    }

    public String fullAddress() {
        return fullAddress;
    }

    public String city() {
        return city;
    }

    public String state() {
        return state;
    }

    public String postalCode() {
        return postalCode;
    }
}
