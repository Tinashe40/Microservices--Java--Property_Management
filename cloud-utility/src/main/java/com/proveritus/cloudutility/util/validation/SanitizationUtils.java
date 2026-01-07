package com.proveritus.cloudutility.util.validation;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public final class SanitizationUtils {

    private static final PolicyFactory POLICY_FACTORY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    private SanitizationUtils() {
    }

    public static String sanitize(String untrustedHTML) {
        return POLICY_FACTORY.sanitize(untrustedHTML);
    }
}
