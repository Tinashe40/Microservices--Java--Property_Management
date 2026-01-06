package com.proveritus.cloudutility.core.application;

@FunctionalInterface
public interface NoInputUseCase<OUTPUT> extends UseCase<Void, OUTPUT> {

    default OUTPUT execute(Void input) {
        return execute();
    }

    OUTPUT execute();
}
