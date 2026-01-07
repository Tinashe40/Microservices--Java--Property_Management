package com.proveritus.cloudutility.observability.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PerformanceMetrics {

    public PerformanceMetrics(MeterRegistry meterRegistry) {
        meterRegistry.counter("performance.metric", "type", "performance");
    }
}