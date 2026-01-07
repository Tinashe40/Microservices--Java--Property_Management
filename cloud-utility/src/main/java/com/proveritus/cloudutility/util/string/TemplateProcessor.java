package com.proveritus.cloudutility.util.string;

import java.util.Map;

public final class TemplateProcessor {

    private TemplateProcessor() {
    }

    public static String process(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }
}