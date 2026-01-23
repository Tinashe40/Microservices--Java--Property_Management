package com.tinash.cloud.utility.ratelimit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rate-limiting")
@Getter
@Setter
public class RateLimitProperties {

    private boolean enabled;
    private int limit;
    private int duration;
}
