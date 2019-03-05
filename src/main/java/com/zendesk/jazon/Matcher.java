package com.zendesk.jazon;

import com.zendesk.jazon.actual.Actual;
import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.expectation.ExpectationFactory;
import com.zendesk.jazon.expectation.JsonExpectation;
import groovy.json.JsonSlurper;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class Matcher {
    private final ExpectationFactory expectationFactory;
    private final ActualFactory actualFactory;
    private JsonExpectation expectation;
    private Actual actual;

    Matcher(ExpectationFactory expectationFactory, ActualFactory actualFactory) {
        this.expectationFactory = expectationFactory;
        this.actualFactory = actualFactory;
    }

    public MatchResult match() {
        return actual.accept(expectation);
    }

    public Matcher expected(JsonExpectation expectation) {
        this.expectation = checkNotNull(expectation);
        return this;
    }

    public Matcher expected(Map<String, Object> expectationMap) {
        this.expectation = expectationFactory.expectation(expectationMap);
        return this;
    }

    public Matcher actual(Actual actual) {
        this.actual = checkNotNull(actual);
        return this;
    }

    public Matcher actual(Map<String, Object> jsonAsMap) {
        this.actual = actualFactory.actual(jsonAsMap);
        return this;
    }

    public Matcher actual(String jsonAsString) {
        Object parsed = new JsonSlurper().parse(jsonAsString.getBytes());
        this.actual = actualFactory.actual(parsed);
        return this;
    }
}
