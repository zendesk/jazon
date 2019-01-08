package com.zendesk;

import groovy.json.JsonSlurper;

import java.util.Map;

import static com.zendesk.ExpectationTranslators.objectExpectation;
import static java.util.stream.Collectors.toMap;

public class FacadeExpectation {
    private final ObjectExpectation objectExpectation;

    public FacadeExpectation(Map<String, Object> expectationMap) {
        this.objectExpectation = objectExpectation(expectationMap);
    }

    public JazonMatchResult match(String jsonAsString) {
        Object parsed = new JsonSlurper().parse(jsonAsString.getBytes());
        // here is "what is actual" responsibility
        return objectExpectation.match(new ActualJsonObject(kek((Map<String, Object>) parsed)));
    }

    public JazonMatchResult match(Map<String, Object> jsonAsMap) {
        return objectExpectation.match(new ActualJsonObject(kek(jsonAsMap)));
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
