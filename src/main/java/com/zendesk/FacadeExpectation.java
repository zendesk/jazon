package com.zendesk;

import groovy.json.JsonSlurper;

import java.util.Map;

import static com.zendesk.ActualTranslators.actualObject;
import static com.zendesk.ExpectationTranslators.objectExpectation;

public class FacadeExpectation {
    private final ObjectExpectation objectExpectation;

    public FacadeExpectation(Map<String, Object> expectationMap) {
        this.objectExpectation = objectExpectation(expectationMap);
    }

    public JazonMatchResult match(String jsonAsString) {
        Object parsed = new JsonSlurper().parse(jsonAsString.getBytes());
        // here is "what is actual" responsibility
        ActualJsonObject actualJsonObject = actualObject((Map<String, Object>) parsed);
        return objectExpectation.match(actualJsonObject);
    }

    public JazonMatchResult match(Map<String, Object> jsonAsMap) {
        // here is "what is actual" responsibility
        ActualJsonObject actualJsonObject = actualObject(jsonAsMap);
        return objectExpectation.match(actualJsonObject);
    }
}
