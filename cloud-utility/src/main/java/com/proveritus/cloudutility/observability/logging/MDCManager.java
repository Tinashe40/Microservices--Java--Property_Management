package com.proveritus.cloudutility.observability.logging;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MDCManager {

    public void set(String key, String value) {
        MDC.put(key, value);
    }

    public void remove(String key) {
        MDC.remove(key);
    }
}