package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@EqualsAndHashCode
@ToString
public class NoFieldMismatch implements Mismatch, MismatchWithPathFactory {
    private final String fieldName;
    private final JsonExpectation expectation;

    public NoFieldMismatch(String fieldName, JsonExpectation expectation) {
        this.fieldName = checkNotNull(fieldName);
        this.expectation = checkNotNull(expectation);
    }
}
