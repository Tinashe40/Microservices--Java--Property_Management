package com.proveritus.cloudutility.core.application;

/**
 * Use case with no output (commands that don't return values).
 */
@FunctionalInterface
public interface VoidUseCase<INPUT> extends UseCase<INPUT, Void> {

    default Void execute(INPUT input) {
        executeVoid(input);
        return null;
    }

    void executeVoid(INPUT input);
}