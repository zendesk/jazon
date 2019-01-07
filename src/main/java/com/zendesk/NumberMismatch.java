package com.zendesk;

import java.util.Objects;

public class NumberMismatch implements JsonMismatch {
    private final Number expected;
    private final Number actual;

    public NumberMismatch(Number expected, Number actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String message() {
        return "ale beka number mismacz";   //FIXME
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberMismatch that = (NumberMismatch) o;
        return Objects.equals(expected, that.expected) &&
                Objects.equals(actual, that.actual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expected, actual);
    }

    @Override
    public String toString() {
        return "NumberMismatch{" +
                "expected=" + expected +
                ", actual=" + actual +
                '}';
    }
}
