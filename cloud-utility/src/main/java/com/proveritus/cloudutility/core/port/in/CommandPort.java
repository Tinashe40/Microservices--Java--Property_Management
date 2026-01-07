package com.proveritus.cloudutility.core.port.in;

public interface CommandPort<I> {

    void execute(I command);
}