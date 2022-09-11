package com.zendesk.jazon.mismatch.impl;

import com.zendesk.jazon.actual.Actual;
import com.zendesk.jazon.mismatch.Mismatch;
import com.zendesk.jazon.mismatch.MismatchWithPathFactory;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class PrimitiveValueMismatch<T extends Actual> implements Mismatch, MismatchWithPathFactory {
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
