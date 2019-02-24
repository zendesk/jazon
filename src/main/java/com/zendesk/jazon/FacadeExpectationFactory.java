package com.zendesk.jazon;

import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.expectation.ExpectationFactory;
import com.zendesk.jazon.expectation.JsonExpectation;
import com.zendesk.jazon.expectation.ObjectExpectation;

import java.util.Map;

public class FacadeExpectationFactory {
    private final ExpectationFactory expectationFactory;
    private final ActualFactory actualFactory;

    public FacadeExpectationFactory(ExpectationFactory expectationFactory, ActualFactory actualFactory) {
        this.expectationFactory = expectationFactory;
        this.actualFactory = actualFactory;
    }

    public FacadeExpectation facadeExpectation(Map<String, Object> expectationMap) {
        JsonExpectation expectation = expectationFactory.expectation(expectationMap);
        return new FacadeExpectation((ObjectExpectation) expectation, actualFactory);
    }
}
