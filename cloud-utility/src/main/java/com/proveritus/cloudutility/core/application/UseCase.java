package com.proveritus.cloudutility.core.application;

/**
 * Marker interface for use cases.
 * Follows Command pattern for better separation of concerns.
 */
public interface UseCase<INPUT, OUTPUT> {
    
    /**
     * Executes the use case.
     *
     * @param input the input command or query
     * @return the result
     */
    OUTPUT execute(INPUT input);
}
