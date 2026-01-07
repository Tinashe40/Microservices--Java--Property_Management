package com.proveritus.cloudutility.observability.tracing;

import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Service;

@Service
public class TracingService {

    private final Tracer tracer;

    public TracingService(Tracer tracer) {
        this.tracer = tracer;
    }

    public Tracer getTracer() {
        return tracer;
    }
}