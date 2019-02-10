package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;

import java.util.Objects;

public class NotNullMismatch implements JsonMismatch {
    private final Actual actual;

    public NotNullMismatch(Actual actual) {
        this.actual = actual;
    }

    @Override
    public String toString() {
        return "NotNullMismatch{" +
                "actual=" + actual +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotNullMismatch that = (NotNullMismatch) o;
        return Objects.equals(actual, that.actual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actual);
    }
}
