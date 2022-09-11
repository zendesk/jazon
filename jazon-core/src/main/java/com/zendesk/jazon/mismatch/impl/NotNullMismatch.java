package com.zendesk.jazon.mismatch.impl;

import com.zendesk.jazon.actual.Actual;
import com.zendesk.jazon.mismatch.Mismatch;
import com.zendesk.jazon.mismatch.MismatchWithPathFactory;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class NotNullMismatch implements Mismatch, MismatchWithPathFactory {
    private final Actual actual;

    public NotNullMismatch(Actual actual) {
        this.actual = checkNotNull(actual);
    }

    @Override
    public String message() {
        return String.format("Expected null. Found: %s", actual);
    }
}
