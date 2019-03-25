package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.empty;

@ToString
@EqualsAndHashCode
public class NullMismatch<T> implements JsonMismatch {
    private final Class<? extends Actual> expectedType;
    private final Optional<T> expectedValue;

    public NullMismatch(Class<? extends Actual> expectedType, T expectedValue) {
        this(expectedType, Optional.of(expectedValue));
    }

    public NullMismatch(Class<? extends Actual> expectedType) {
        this(expectedType, empty());
    }

    private NullMismatch(Class<? extends Actual> expectedType, Optional<T> expectedValue) {
        this.expectedType = checkNotNull(expectedType);
        this.expectedValue = checkNotNull(expectedValue);
    }

    @Override
    public String message() {
        String expectationMessage = expectedValue
                .map(value -> String.format("%s (type: %s)", value, expectedType))
                .orElseGet(expectedType::toString);
        return String.format("Found null. Expected: %s", expectationMessage);
    }
}
