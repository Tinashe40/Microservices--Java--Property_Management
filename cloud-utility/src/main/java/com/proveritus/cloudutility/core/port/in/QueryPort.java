package com.proveritus.cloudutility.core.port.in;

public interface QueryPort<I, O> {

    O execute(I query);
}