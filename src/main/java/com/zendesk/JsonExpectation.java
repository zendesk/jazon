package com.zendesk;

import groovy.json.JsonSlurper;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class JsonExpectation {
    private final Map<String, JsonExpectationBeka> expectationMap;

    public JsonExpectation(Map<String, JsonExpectationBeka> expectationMap) {
        this.expectationMap = expectationMap;
    }

    public JazonMatchResult match(String jsonAsString) {
        Object parsed = new JsonSlurper().parse(jsonAsString.getBytes());
        // here is "what is actual" responsibility
        return new ObjectExpectation(expectationMap)
                .match(new ActualJsonObject(kek((Map<String, Object>) parsed)));
    }

    public JazonMatchResult match(Map<String, Object> jsonAsMap) {
        return new ObjectExpectation(expectationMap)
                .match(new ActualJsonObject(kek(jsonAsMap)));
    }

    // "what is actual" responsibility used in iteration
    private Map<String, Actual> kek(Map<String, Object> jsonAsMap) {
        return jsonAsMap
                .entrySet()
                .stream()
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                e -> new ActualJsonNumber((Number) e.getValue())
                        )
                );
    }
}
