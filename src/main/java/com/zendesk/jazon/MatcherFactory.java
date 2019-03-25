package com.zendesk.jazon;

import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.actual.DefaultActualFactory;
import com.zendesk.jazon.expectation.DefaultExpectationFactory;
import com.zendesk.jazon.expectation.ExpectationFactory;

public class MatcherFactory {
    private final ExpectationFactory expectationFactory;
    private final ActualFactory actualFactory;

    public MatcherFactory(ExpectationFactory expectationFactory, ActualFactory actualFactory) {
        this.expectationFactory = expectationFactory;
        this.actualFactory = actualFactory;
    }

    public MatcherFactory() {
        this(new DefaultExpectationFactory(), new DefaultActualFactory());
    }

    public Matcher matcher() {
        return new Matcher(expectationFactory, actualFactory);
    }
}
