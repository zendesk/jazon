package com.zendesk;

import java.util.Objects;

public class SizesJsonMismatch implements JsonMismatch {
    private final int expectedSize;
    private final int actualSize;

    SizesJsonMismatch(int expectedSize, int actualSize) {
        this.expectedSize = expectedSize;
        this.actualSize = actualSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SizesJsonMismatch that = (SizesJsonMismatch) o;
        return expectedSize == that.expectedSize &&
                actualSize == that.actualSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectedSize, actualSize);
    }

    @Override
    public String toString() {
        return "SizesJsonMismatch{" +
                "expectedSize=" + expectedSize +
                ", actualSize=" + actualSize +
                '}';
    }
}
