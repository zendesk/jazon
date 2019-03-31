package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

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
