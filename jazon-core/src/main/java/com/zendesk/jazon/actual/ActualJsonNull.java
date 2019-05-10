package com.zendesk.jazon.actual;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

public enum ActualJsonNull implements Actual {
    INSTANCE;

    @Override
    public MatchResult accept(JsonExpectation expectation, String path) {
        return expectation.match(this, path);
    }

    @Override
    public String toString() {
        return "null";
    }
}
