package com.proveritus.cloudutility.core.application;

public interface UseCase<I, O> {

    O execute(I input);
}
