package com.zendesk.jazon;

import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.actual.ObjectsActualFactory;
import com.zendesk.jazon.expectation.DefaultExpectationFactory;
import com.zendesk.jazon.expectation.ExpectationFactory;

public class MatcherFactory {
    private final ExpectationFactory expectationFactory;
    private final ActualFactory<Object> actualFactory;

    public MatcherFactory(ExpectationFactory expectationFactory, ActualFactory<Object> actualFactory) {
        this.expectationFactory = expectationFactory;
        this.actualFactory = actualFactory;
    }

    public MatcherFactory() {
        this(new DefaultExpectationFactory(), new ObjectsActualFactory());
    }

    public Matcher matcher() {
        return new Matcher(expectationFactory, actualFactory);
    }
}
