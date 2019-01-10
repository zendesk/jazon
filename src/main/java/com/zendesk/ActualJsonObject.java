package com.zendesk;

import java.util.Map;

class ActualJsonObject implements Actual {
    private final Map<String, Actual> map;

    ActualJsonObject(Map<String, Actual> map) {
        this.map = map;
    }

    public Map<String, Actual> map() {
        return map;
    }

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }
}
