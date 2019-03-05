package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

public enum ActualJsonNull implements Actual {
    INSTANCE;

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }

    @Override
    public String toString() {
        return "null";
    }
}
