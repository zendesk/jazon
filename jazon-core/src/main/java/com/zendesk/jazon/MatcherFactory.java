package com.zendesk.jazon;

import com.zendesk.jazon.actual.factory.ActualFactory;
import com.zendesk.jazon.expectation.TranslatorFacade;

public class MatcherFactory {
    private final TranslatorFacade translator;
    private final ActualFactory<String> actualFactory;

    public MatcherFactory(TranslatorFacade translator, ActualFactory<String> actualFactory) {
        this.translator = translator;
        this.actualFactory = actualFactory;
    }

    public Matcher matcher() {
        return new Matcher(translator, actualFactory);
    }
}
