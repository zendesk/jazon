package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class PrimitiveValueMismatch<T extends Actual> implements Mismatch, MismatchOccurrenceFactory {
    private final T expected;
    private final T actual;

    public PrimitiveValueMismatch(T expected, T actual) {
        this.expected = checkNotNull(expected);
        this.actual = checkNotNull(actual);
    }

    @Override
    public String message() {
        return String.format("Expected: %s\nActual:   %s", expected, actual);
    }
}
