package com.zendesk.jazon.mismatch;

import java.util.Objects;

public class ArrayElementMismatch implements JsonMismatch {
    private final int elementIndex;
    private final JsonMismatch elementMismatch;

    public ArrayElementMismatch(int elementIndex, JsonMismatch elementMismatch) {
        this.elementIndex = elementIndex;
        this.elementMismatch = elementMismatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayElementMismatch that = (ArrayElementMismatch) o;
        return elementIndex == that.elementIndex &&
                Objects.equals(elementMismatch, that.elementMismatch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementIndex, elementMismatch);
    }

    @Override
    public String toString() {
        return "ArrayElementMismatch{" +
                "elementIndex=" + elementIndex +
                ", elementMismatch=" + elementMismatch +
                '}';
    }
}
