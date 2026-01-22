package com.proveritus.cloudutility.util;

import com.tinash.cloud.utility.util.string.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @Test
    void testStringUtils() {
        assertTrue(StringUtils.isBlank(""));
    }
}
