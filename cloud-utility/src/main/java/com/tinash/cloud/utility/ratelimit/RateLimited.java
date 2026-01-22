package com.tinash.cloud.utility.ratelimit;

import com.tinash.cloud.utility.constant.AppConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Custom annotation to enable API rate limiting on specific methods.
 * When applied to a method (typically a REST controller method), an AOP aspect
 * will intercept the call and enforce rate limits defined by this annotation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {

    /**
     * Defines the maximum number of requests allowed within a given time period.
     * Defaults to {@link AppConstants#DEFAULT_RATE_LIMIT_CAPACITY}.
     *
     * @return The capacity of the rate limit bucket.
     */
    long capacity() default AppConstants.DEFAULT_RATE_LIMIT_CAPACITY;

    /**
     * Defines the duration of the time period for the rate limit.
     * Defaults to {@link AppConstants#DEFAULT_RATE_LIMIT_PERIOD_MINUTES} minutes.
     *
     * @return The duration of the rate limit period.
     */
    long period() default AppConstants.DEFAULT_RATE_LIMIT_PERIOD_MINUTES;

    /**
     * Defines the time unit for the rate limit period.
     * Defaults to minutes.
     *
     * @return The TimeUnit for the rate limit period.
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * Defines the key used to identify the rate limit bucket.
     * This can be a SpEL expression to dynamically generate the key (e.g., "#ip" for IP-based limiting).
     * If empty, a default key based on method signature will be used.
     *
     * @return The key for the rate limit bucket.
     */
    String key() default "";

    /**
     * Defines the error message to return when the rate limit is exceeded.
     *
     * @return The custom error message.
     */
    String errorMessage() default "Too many requests. Please try again later.";
}
