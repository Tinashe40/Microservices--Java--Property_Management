package com.proveritus.cloudutility.core.common;

import java.util.Optional;
import java.util.function.Function;

public final class Result<V, E> {

    private final V value;
    private final E error;

    private Result(V value, E error) {
        this.value = value;
        this.error = error;
    }

    public static <V, E> Result<V, E> success(V value) {
        return new Result<>(value, null);
    }

    public static <V, E> Result<V, E> failure(E error) {
        return new Result<>(null, error);
    }

    public boolean isSuccess() {
        return value != null;
    }

    public boolean isFailure() {
        return error != null;
    }

    public Optional<V> getValue() {
        return Optional.ofNullable(value);
    }

    public Optional<E> getError() {
        return Optional.ofNullable(error);
    }

    public <U> Result<U, E> map(Function<V, U> mapper) {
        if (isSuccess()) {
            return Result.success(mapper.apply(value));
        }
        return Result.failure(error);
    }

    public <U> Result<U, E> flatMap(Function<V, Result<U, E>> mapper) {
        if (isSuccess()) {
            return mapper.apply(value);
        }
        return Result.failure(error);
    }
}
