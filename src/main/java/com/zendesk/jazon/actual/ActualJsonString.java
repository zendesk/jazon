package com.zendesk.jazon.actual;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;

import static com.google.common.base.Preconditions.checkNotNull;

@EqualsAndHashCode
public class ActualJsonString implements Actual {
    private final String string;

    public ActualJsonString(String string) {
        this.string = checkNotNull(string);
    }

    public String string() {
        return string;
    }

    @Override
    public MatchResult accept(JsonExpectation expectation, String path) {
        return expectation.match(this, path);
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", string);
    }
}
