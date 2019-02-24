package com.zendesk.jazon;

import com.zendesk.jazon.actual.Actual;
import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.expectation.ObjectExpectation;
import groovy.json.JsonSlurper;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class FacadeExpectation {
    private final ObjectExpectation objectExpectation;
    private final ActualFactory actualFactory;

    public FacadeExpectation(ObjectExpectation objectExpectation, ActualFactory actualFactory) {
        this.objectExpectation = checkNotNull(objectExpectation);
        this.actualFactory = checkNotNull(actualFactory);
    }

    public JazonMatchResult match(String jsonAsString) {
        Object parsed = new JsonSlurper().parse(jsonAsString.getBytes());
        Actual actual = actualFactory.actual(parsed);
        return actual.accept(objectExpectation);
    }

    public JazonMatchResult match(Map<String, Object> jsonAsMap) {
        Actual actual = actualFactory.actual(jsonAsMap);
        return actual.accept(objectExpectation);
    }
}
