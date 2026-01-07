package com.proveritus.cloudutility.observability.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class BusinessMetrics {

    public BusinessMetrics(MeterRegistry meterRegistry) {
        meterRegistry.counter("business.metric", "type", "business");
    }
}