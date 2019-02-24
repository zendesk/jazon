package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class ArrayUnexpectedElementsMismatch implements JsonMismatch {
    private final List<Actual> unexpectedElements;

    public ArrayUnexpectedElementsMismatch(List<Actual> unexpectedElements) {
        this.unexpectedElements = checkNotNull(unexpectedElements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayUnexpectedElementsMismatch that = (ArrayUnexpectedElementsMismatch) o;
        return Objects.equals(unexpectedElements, that.unexpectedElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unexpectedElements);
    }

    @Override
    public String toString() {
        return "ArrayUnexpectedElementsMismatch{" +
                "unexpectedElements=" + unexpectedElements +
                '}';
    }
}
