package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActualJsonNumber implements Actual {
    private final Number number;

    ActualJsonNumber(Number number) {
        this.number = checkNotNull(number);
    }

    public Number number() {
        return number;
    }

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActualJsonNumber that = (ActualJsonNumber) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
