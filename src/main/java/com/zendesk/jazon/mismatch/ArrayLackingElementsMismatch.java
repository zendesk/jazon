package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.expectation.JsonExpectation;

import java.util.List;
import java.util.Objects;

public class ArrayLackingElementsMismatch implements JsonMismatch {
    private final List<JsonExpectation> lackingElements;

    public ArrayLackingElementsMismatch(List<JsonExpectation> lackingElements) {
        this.lackingElements = lackingElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayLackingElementsMismatch that = (ArrayLackingElementsMismatch) o;
        return Objects.equals(lackingElements, that.lackingElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lackingElements);
    }

    @Override
    public String toString() {
        return "ArrayLackingElementsMismatch{" +
                "lackingElements=" + lackingElements +
                '}';
    }
}
