package com.zendesk.jazon;

import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.expectation.ExpectationFactory;

public class MatcherFactory {
    private final ExpectationFactory expectationFactory;
    private final ActualFactory<String> actualFactory;

    public MatcherFactory(ExpectationFactory expectationFactory, ActualFactory<String> actualFactory) {
        this.expectationFactory = expectationFactory;
        this.actualFactory = actualFactory;
    }

    public Matcher matcher() {
        return new Matcher(expectationFactory, actualFactory);
    }
}
