package com.zendesk.jazon.spock

import com.zendesk.jazon.MatcherFactory
import com.zendesk.jazon.actual.DefaultActualFactory
import com.zendesk.jazon.expectation.SpockExpectationFactory
import groovy.json.JsonSlurper

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
                .actual(parsed(json))
                .match();
        if (matchResult.ok()) {
            return true
        }
        throw new AssertionError("\n\n${matchResult.message()}\n")
    }

    private static Map<String, Object> parsed(String jsonAsString) {
        new JsonSlurper()
                .parse(jsonAsString.getBytes()) as Map<String, Object>
    }

    static JazonSpockAdapter jazon(String json) {
        return new JazonSpockAdapter(json)
    }
}
