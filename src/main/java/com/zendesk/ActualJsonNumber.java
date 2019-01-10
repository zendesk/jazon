package com.zendesk;

class ActualJsonNumber implements Actual {
    private final Number number;

    ActualJsonNumber(Number number) {
        this.number = number;
    }

    public Number number() {
        return number;
    }

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }
}
