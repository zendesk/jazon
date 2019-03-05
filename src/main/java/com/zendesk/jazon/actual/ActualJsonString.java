package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;

import static com.google.common.base.Preconditions.checkNotNull;

@EqualsAndHashCode
public class ActualJsonString implements Actual {
    private final String string;

    ActualJsonString(String string) {
        this.string = checkNotNull(string);
    }

    public String string() {
        return string;
    }

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }

    @Override
    public String toString() {
        return string;
    }
}
