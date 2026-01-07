package com.proveritus.cloudutility.core.specification;

/**
 * Specification pattern for complex query building.
 * Follows OCP (Open/Closed Principle).
 *
 * @param <T> the entity type
 */
@FunctionalInterface
public interface Specification<T> {
    
    boolean isSatisfiedBy(T entity);
    
    default Specification<T> and(Specification<T> other) {
        return entity -> this.isSatisfiedBy(entity) && other.isSatisfiedBy(entity);
    }
    
    default Specification<T> or(Specification<T> other) {
        return entity -> this.isSatisfiedBy(entity) || other.isSatisfiedBy(entity);
    }
    
    default Specification<T> not() {
        return entity -> !this.isSatisfiedBy(entity);
    }
}
