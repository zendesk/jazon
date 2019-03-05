package com.zendesk.jazon.spock

import com.zendesk.jazon.MatcherFactory
import com.zendesk.jazon.actual.DefaultActualFactory
import com.zendesk.jazon.expectation.SpockExpectationFactory

class JazonSpockAdapter {
    MatcherFactory matcherFactory = new MatcherFactory(
            new SpockExpectationFactory(),
            new DefaultActualFactory()
    )
    private final String json

    private JazonSpockAdapter(String json) {
        this.json = json
    }

    boolean matches(Map jsonAsMap) {
        def matchResult = matcherFactory.matcher()
                .expected(jsonAsMap)
                .actual(json)
                .match();
        if (matchResult.ok()) {
            return true
        }
        throw new AssertionError("\n\n${matchResult.message()}\n")
    }

    static JazonSpockAdapter jazon(String json) {
        return new JazonSpockAdapter(json)
    }
}
