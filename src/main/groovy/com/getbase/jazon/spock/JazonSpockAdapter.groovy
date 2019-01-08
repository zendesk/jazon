package com.getbase.jazon.spock


import com.zendesk.FacadeExpectation

class JazonSpockAdapter {
    private final String json

    private JazonSpockAdapter(String json) {
        this.json = json
    }

    boolean matches(Map jsonAsMap) {
        def matchResult = new FacadeExpectation(jsonAsMap).match(json)
        if (matchResult.ok()) {
            return true
        }
        throw new AssertionError("\n\n${matchResult.message()}\n")
    }

    static JazonSpockAdapter jazon(String json) {
        return new JazonSpockAdapter(json)
    }
}
