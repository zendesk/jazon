package com.zendesk.jazon.mismatch.impl;

import com.zendesk.jazon.expectation.JsonExpectation;
import com.zendesk.jazon.mismatch.Mismatch;
import com.zendesk.jazon.mismatch.MismatchWithPathFactory;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@EqualsAndHashCode
@ToString
public class NoFieldMismatch implements Mismatch, MismatchWithPathFactory {
    private final String fieldName;
    private final JsonExpectation expectation;

    public NoFieldMismatch(String fieldName, JsonExpectation expectation) {
        this.fieldName = checkNotNull(fieldName);
        this.expectation = checkNotNull(expectation);
    }

    @Override
    public String message() {
        return String.format("Could not find expected field (\"%s\": %s)", fieldName, expectation);
    }
}
