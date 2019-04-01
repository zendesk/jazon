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
        return match(jsonAsMap, parsed(json) as Map<String, Object>)
    }

    boolean matches(List jsonAsList) {
        return match(jsonAsList, parsed(json) as List<Object>)
    }

    private boolean match(Object expected, Object actual) {
        def matchResult = matcherFactory.matcher()
                .expected(expected)
                .actual(actual)
                .match()
        if (matchResult.ok()) {
            return true
        }
        throw new AssertionError("\n-----------------------------------\nJSON MISMATCH:\n${matchResult.message()}\n-----------------------------------\n")
    }

    private static parsed(String jsonAsString) {
        new JsonSlurper()
                .parse(jsonAsString.getBytes())
    }

    static JazonSpockAdapter jazon(String json) {
        return new JazonSpockAdapter(json)
    }
}
