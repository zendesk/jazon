package com.zendesk;

class ActualJsonNull implements Actual {
    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }
}
