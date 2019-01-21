package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

public class ActualJsonString implements Actual {
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
