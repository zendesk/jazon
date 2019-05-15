package com.zendesk.jazon;

import com.zendesk.jazon.actual.Actual;
import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.expectation.ExpectationFactory;
import com.zendesk.jazon.expectation.JsonExpectation;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

public class Matcher {
    private final ExpectationFactory expectationFactory;
    private final ActualFactory<Object> actualFactory;
    private JsonExpectation expectation;
    private Actual actual;

    Matcher(ExpectationFactory expectationFactory, ActualFactory<Object> actualFactory) {
        this.expectationFactory = expectationFactory;
        this.actualFactory = actualFactory;
    }

    public MatchResult match() {
        return actual.accept(expectation, "$");
    }

    public Matcher expected(JsonExpectation expectation) {
        this.expectation = checkNotNull(expectation);
        return this;
    }

    public Matcher expected(Object expectation) {
        this.expectation = expectationFactory.expectation(expectation);
        return this;
    }

    public Matcher actual(Actual actual) {
        this.actual = checkNotNull(actual);
        return this;
    }

    public Matcher actual(Object actual) {
        this.actual = actualFactory.actual(actual);
        return this;
    }
}
