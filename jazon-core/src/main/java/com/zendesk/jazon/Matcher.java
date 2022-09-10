package com.zendesk.jazon;

import com.zendesk.jazon.actual.Actual;
import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.expectation.JsonExpectation;
import com.zendesk.jazon.expectation.TranslatorFacade;

public class Matcher {
    private final TranslatorFacade translator;
    private final ActualFactory<String> actualFactory;
    private JsonExpectation expectation;
    private Actual actual;

    Matcher(TranslatorFacade translator, ActualFactory<String> actualFactory) {
        this.translator = translator;
        this.actualFactory = actualFactory;
    }

    public MatchResult match() {
        return actual.accept(expectation, "$");
    }

    public Matcher expected(Object expectation) {
        this.expectation = translator.expectation(expectation);
        return this;
    }

    public Matcher actual(String actualJsonString) {
        this.actual = actualFactory.actual(actualJsonString);
        return this;
    }
}
