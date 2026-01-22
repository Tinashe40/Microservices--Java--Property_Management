package com.tinash.cloud.utility.ratelimit;

import com.tinash.cloud.utility.exception.TooManyRequestsException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Aspect for enforcing API rate limits using the {@link RateLimited} annotation.
 * It intercepts method calls, checks for available tokens in a Bucket4j bucket,
 * and throws a {@link TooManyRequestsException} if the rate limit is exceeded.
 */
@Aspect
@Component
public class RateLimitAspect {

    private final BucketManager bucketManager;
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    public RateLimitAspect(BucketManager bucketManager) {
        this.bucketManager = bucketManager;
    }

    @Around("@annotation(com.tinash.cloud.utility.ratelimit.RateLimited)")
    public Object rateLimitAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimited rateLimited = method.getAnnotation(RateLimited.class);

        String key = resolveRateLimitKey(rateLimited.key(), joinPoint);

        // Get the bucket for the resolved key
        Bucket bucket = bucketManager.getBucket(
                key,
                rateLimited.capacity(),
                rateLimited.capacity(), // Refill tokens equal to capacity for simplicity, or define separate refill rate
                Duration.of(rateLimited.period(), rateLimited.unit().toChronoUnit())
        );

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            // Add headers for remaining tokens and retry-after if needed
            // For example:
            // HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            // response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return joinPoint.proceed();
        } else {
            // Calculate retry-after seconds
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            throw new TooManyRequestsException(rateLimited.errorMessage(), waitForRefill);
        }
    }

    /**
     * Resolves the rate limit key. If a SpEL expression is provided, it evaluates it.
     * Otherwise, it constructs a key from the method signature.
     *
     * @param spelExpression The SpEL expression to evaluate, or empty string.
     * @param joinPoint The join point.
     * @return The resolved rate limit key.
     */
    private String resolveRateLimitKey(String spelExpression, ProceedingJoinPoint joinPoint) {
        if (!spelExpression.isEmpty()) {
            EvaluationContext context = new StandardEvaluationContext();
            // Add method arguments to evaluation context
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = methodSignature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
            // Add HttpServletRequest to context for IP-based limiting etc.
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                context.setVariable("ip", request.getRemoteAddr());
                context.setVariable("headers", request.getHeaderNames());
                context.setVariable("url", request.getRequestURI());
            }

            return expressionParser.parseExpression(spelExpression).getValue(context, String.class);
        } else {
            // Default key: class name + method name
            return joinPoint.getSignature().toShortString();
        }
    }
}
