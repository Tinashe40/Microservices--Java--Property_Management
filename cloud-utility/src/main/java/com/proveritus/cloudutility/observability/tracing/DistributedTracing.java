package com.proveritus.cloudutility.observability.tracing;

import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Component;

@Component
public class DistributedTracing {

    private final Tracer tracer;

    public DistributedTracing(Tracer tracer) {
        this.tracer = tracer;
    }

    public Tracer getTracer() {
        return tracer;
    }
}