package com.zendesk.jazon;

import com.zendesk.jazon.actual.Actual;
import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.expectation.ExpectationFactory;
import com.zendesk.jazon.expectation.JsonExpectation;

public class Matcher {
    private final ExpectationFactory expectationFactory;
    private final ActualFactory<String> actualFactory;
    private JsonExpectation expectation;
    private Actual actual;

    Matcher(ExpectationFactory expectationFactory, ActualFactory<String> actualFactory) {
        this.expectationFactory = expectationFactory;
        this.actualFactory = actualFactory;
    }

    public MatchResult match() {
        return actual.accept(expectation, "$");
    }

    public Matcher expected(Object expectation) {
        this.expectation = expectationFactory.expectation(expectation);
        return this;
    }

//    public Matcher actual(Actual actual) {
//        this.actual = checkNotNull(actual);
//        return this;
//    }

//    public Matcher actual(Object actual) {
//        this.actual = actualFactory.actual(actual);
//        return this;
//    }

    public Matcher actual(String actualJsonString) {
        this.actual = actualFactory.actual(actualJsonString);
        return this;
    }
}
