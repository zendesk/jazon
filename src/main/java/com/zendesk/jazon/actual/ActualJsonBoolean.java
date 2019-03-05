package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ActualJsonBoolean implements Actual {
    private final boolean value;

    ActualJsonBoolean(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }
}
