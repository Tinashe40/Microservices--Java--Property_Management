package com.proveritus.cloudutility.core.common;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Result wrapper following Railway Oriented Programming.
 * Represents either success or failure without throwing exceptions.
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error
 */
public sealed interface Result<T, E> permits Result.Success, Result.Failure {
    
    static <T, E> Result<T, E> success(T value) {
        return new Success<>(value);
    }
    
    static <T, E> Result<T, E> failure(E error) {
        return new Failure<>(error);
    }
    
    boolean isSuccess();
    
    boolean isFailure();
    
    Optional<T> getValue();
    
    Optional<E> getError();
    
    <U> Result<U, E> map(Function<T, U> mapper);
    
    <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper);
    
    Result<T, E> onSuccess(Consumer<T> action);
    
    Result<T, E> onFailure(Consumer<E> action);
    
    record Success<T, E>(T value) implements Result<T, E> {
        public Success {
            Objects.requireNonNull(value, "Success value cannot be null");
        }
        
        @Override
        public boolean isSuccess() {
            return true;
        }
        
        @Override
        public boolean isFailure() {
            return false;
        }
        
        @Override
        public Optional<T> getValue() {
            return Optional.of(value);
        }
        
        @Override
        public Optional<E> getError() {
            return Optional.empty();
        }
        
        @Override
        public <U> Result<U, E> map(Function<T, U> mapper) {
            return success(mapper.apply(value));
        }
        
        @Override
        public <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper) {
            return mapper.apply(value);
        }
        
        @Override
        public Result<T, E> onSuccess(Consumer<T> action) {
            action.accept(value);
            return this;
        }
        
        @Override
        public Result<T, E> onFailure(Consumer<E> action) {
            return this;
        }
    }
    
    record Failure<T, E>(E error) implements Result<T, E> {
        public Failure {
            Objects.requireNonNull(error, "Failure error cannot be null");
        }
        
        @Override
        public boolean isSuccess() {
            return false;
        }
        
        @Override
        public boolean isFailure() {
            return true;
        }
        
        @Override
        public Optional<T> getValue() {
            return Optional.empty();
        }
        
        @Override
        public Optional<E> getError() {
            return Optional.of(error);
        }
        
        @Override
        public <U> Result<U, E> map(Function<T, U> mapper) {
            return failure(error);
        }
        
        @Override
        public <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper) {
            return failure(error);
        }
        
        @Override
        public Result<T, E> onSuccess(Consumer<T> action) {
            return this;
        }
        
        @Override
        public Result<T, E> onFailure(Consumer<E> action) {
            action.accept(error);
            return this;
        }
    }
}
