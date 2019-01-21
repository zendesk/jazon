package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;

import java.util.Objects;

public class TypeMismatch implements JsonMismatch {
    private final Class<? extends Actual> expectedType;
    private final Class<? extends Actual> actualType;

    public TypeMismatch(Class<? extends Actual> expectedType, Class<? extends Actual> actualType) {
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    @Override
    public String message() {
        return "types do not match";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeMismatch that = (TypeMismatch) o;
        return Objects.equals(expectedType, that.expectedType) &&
                Objects.equals(actualType, that.actualType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectedType, actualType);
    }

    @Override
    public String toString() {
        return "TypeMismatch{" +
                "expectedType=" + expectedType +
                ", actualType=" + actualType +
                '}';
    }
}
