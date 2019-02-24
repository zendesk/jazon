package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class UnexpectedFieldMismatch<T> implements JsonMismatch {
    private final Class<? extends Actual> actualType;

    public UnexpectedFieldMismatch(Class<? extends Actual> actualType) {
        this.actualType = checkNotNull(actualType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnexpectedFieldMismatch<?> that = (UnexpectedFieldMismatch<?>) o;
        return Objects.equals(actualType, that.actualType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actualType);
    }

    @Override
    public String toString() {
        return "UnexpectedFieldMismatch{" +
                "actualType=" + actualType +
                '}';
    }
}
