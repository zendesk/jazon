package com.zendesk.jazon.spock


import com.zendesk.jazon.FacadeExpectationFactory
import com.zendesk.jazon.actual.DefaultActualFactory
import com.zendesk.jazon.expectation.SpockExpectationFactory

class JazonSpockAdapter {
    FacadeExpectationFactory facadeExpectationFactory = new FacadeExpectationFactory(
            new SpockExpectationFactory(),
            new DefaultActualFactory()
    )
    private final String json

    private JazonSpockAdapter(String json) {
        this.json = json
    }

    boolean matches(Map jsonAsMap) {
        def facadeExpectation = facadeExpectationFactory.facadeExpectation(jsonAsMap)
        def matchResult = facadeExpectation.match(json)
        if (matchResult.ok()) {
            return true
        }
        throw new AssertionError("\n\n${matchResult.message()}\n")
    }

    static JazonSpockAdapter jazon(String json) {
        return new JazonSpockAdapter(json)
    }
}
