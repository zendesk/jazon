package com.zendesk.jazon.mismatch;

import java.util.Objects;

public class PrimitiveValueMismatch<T> implements JsonMismatch {
    private final T expected;
    private final T actual;

    public PrimitiveValueMismatch(T expected, T actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String message() {
        return "ale beka primitive mismacz";   //FIXME
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimitiveValueMismatch that = (PrimitiveValueMismatch) o;
        return Objects.equals(expected, that.expected) &&
                Objects.equals(actual, that.actual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expected, actual);
    }

    @Override
    public String toString() {
        return "PrimitiveValueMismatch{" +
                "expected=" + expected +
                ", actual=" + actual +
                '}';
    }
}
