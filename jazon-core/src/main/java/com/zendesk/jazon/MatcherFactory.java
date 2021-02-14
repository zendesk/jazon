package com.zendesk.jazon;

import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.expectation.TranslatorToExpectation;

public class MatcherFactory {
    private final TranslatorToExpectation translator;
    private final ActualFactory<String> actualFactory;

    public MatcherFactory(TranslatorToExpectation translator, ActualFactory<String> actualFactory) {
        this.translator = translator;
        this.actualFactory = actualFactory;
    }

    public Matcher matcher() {
        return new Matcher(translator, actualFactory);
    }
}
