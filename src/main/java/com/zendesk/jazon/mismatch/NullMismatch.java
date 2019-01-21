package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.empty;

public class NullMismatch<T> implements JsonMismatch {
    private final Class<? extends Actual> expectedType;
    private final Optional<T> expectedValue;

    public NullMismatch(Class<? extends Actual> expectedType, T expectedValue) {
        this(expectedType, Optional.of(expectedValue));
    }

    public NullMismatch(Class<? extends Actual> expectedType) {
        this(expectedType, empty());
    }

    private NullMismatch(Class<? extends Actual> expectedType, Optional<T> expectedValue) {
        this.expectedType = expectedType;
        this.expectedValue = expectedValue;
    }

    @Override
    public String message() {
        String expectationMessage = expectedValue
                .map(value -> String.format("%s (type: %s)", value, expectedType))
                .orElseGet(expectedType::toString);
        return String.format("Found null. Expected: %s", expectationMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NullMismatch<?> that = (NullMismatch<?>) o;
        return Objects.equals(expectedValue, that.expectedValue) &&
                Objects.equals(expectedType, that.expectedType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectedValue, expectedType);
    }

    @Override
    public String toString() {
        return "NullMismatch{" +
                "expectedValue=" + expectedValue +
                ", expectedType=" + expectedType +
                '}';
    }
}
