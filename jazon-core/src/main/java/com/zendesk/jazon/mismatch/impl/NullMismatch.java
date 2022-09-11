package com.zendesk.jazon.mismatch.impl;

import com.zendesk.jazon.expectation.JsonExpectation;
import com.zendesk.jazon.mismatch.Mismatch;
import com.zendesk.jazon.mismatch.MismatchWithPathFactory;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

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
