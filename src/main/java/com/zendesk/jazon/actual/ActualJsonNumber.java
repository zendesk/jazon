package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

public class ActualJsonNumber implements Actual {
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
