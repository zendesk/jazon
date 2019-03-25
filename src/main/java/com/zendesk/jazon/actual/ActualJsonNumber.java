package com.zendesk.jazon.actual;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;

import static com.google.common.base.Preconditions.checkNotNull;

@EqualsAndHashCode
public class ActualJsonNumber implements Actual {
    private final Number number;

    ActualJsonNumber(Number number) {
        this.number = checkNotNull(number);
    }

    public Number number() {
        return number;
    }

    @Override
    public MatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }

    @Override
    public String toString() {
        return number.toString();
    }
}
