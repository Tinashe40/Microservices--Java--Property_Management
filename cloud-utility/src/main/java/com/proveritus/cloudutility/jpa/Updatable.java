package com.proveritus.cloudutility.jpa;

/**
 * An interface to be implemented by update DTOs or commands.
 * It ensures that the object can provide the ID of the domain to be updated.
 */

public interface Updatable {
    Long getId();
}
