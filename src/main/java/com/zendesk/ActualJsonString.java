package com.zendesk;

class ActualJsonString implements Actual {
    private final String string;

    ActualJsonString(String string) {
        this.string = string;
    }

    public String string() {
        return string;
    }

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }
}
