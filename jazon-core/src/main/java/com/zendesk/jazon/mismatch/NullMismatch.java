package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class NullMismatch<T extends JsonExpectation> implements Mismatch, MismatchWithPathFactory {
    private final T expectedValue;

    public NullMismatch(T expectedValue) {
        this.expectedValue = checkNotNull(expectedValue);
    }

    @Override
    public String message() {
        return String.format("Found null. Expected: %s", expectedValue);
    }
}
