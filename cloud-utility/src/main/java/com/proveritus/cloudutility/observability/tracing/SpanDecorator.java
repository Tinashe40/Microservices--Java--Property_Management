package com.proveritus.cloudutility.observability.tracing;

import io.micrometer.tracing.Span;
import org.springframework.stereotype.Component;

@Component
public class SpanDecorator {

    public void decorate(Span span) {
        span.tag("custom.tag", "custom.value");
    }
}