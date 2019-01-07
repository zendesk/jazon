package com.zendesk;

import groovy.json.JsonSlurper;

import java.util.Map;

import static com.zendesk.JazonMatchResult.failure;
import static com.zendesk.JazonMatchResult.success;

public class JsonExpectation {
    private final Map<String, Object> expectationMap;

    public JsonExpectation(Map<String, Object> expectationMap) {
        this.expectationMap = expectationMap;
    }

    public JazonMatchResult match(String jsonAsString) {
        Object parsed = new JsonSlurper().parse(jsonAsString.getBytes());
        // here is "what is actual" responsibility
        return this.match((Map<String, Object>) parsed);
    }

    public JazonMatchResult match(Map<String, Object> jsonAsMap) {
        boolean sizesMatch = expectationMap.size() == jsonAsMap.size();
        if (sizesMatch) {
            return success();
        }
        return failure(
                new SizesJsonMismatch(expectationMap.size(), jsonAsMap.size())
        );
    }
}
