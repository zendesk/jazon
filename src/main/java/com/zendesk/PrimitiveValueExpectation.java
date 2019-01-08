package com.zendesk;

import java.util.Objects;

import static com.zendesk.JazonMatchResult.*;

class PrimitiveValueExpectation<T> implements JsonExpectation {
    private final T expectedValue;

    PrimitiveValueExpectation(T expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    public JazonMatchResult match(ActualJsonNumber actualNumber) {
        if (expectedValue.equals(actualNumber.number())) {
            return success();
        }
        return failure(new PrimitiveValueMismatch<>(expectedValue, actualNumber.number()));
    }

    @Override
    public JazonMatchResult match(ActualJsonObject actualObject) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimitiveValueExpectation that = (PrimitiveValueExpectation) o;
        return Objects.equals(expectedValue, that.expectedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectedValue);
    }

    @Override
    public String toString() {
        return "PrimitiveValueExpectation{" +
                "expectedValue=" + expectedValue +
                '}';
    }
}
