package com.zendesk;

import java.util.Objects;

public class UnexpectedValueMismatch implements JsonMismatch {
    private final Object value;

    public UnexpectedValueMismatch(Object value) {
        this.value = value;
    }

    @Override
    public String message() {
        return "ale beka. should be null a nie jest";    //FIXME
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnexpectedValueMismatch that = (UnexpectedValueMismatch) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "UnexpectedValueMismatch{" +
                "value=" + value +
                '}';
    }
}
