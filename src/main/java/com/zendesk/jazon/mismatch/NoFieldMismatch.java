package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@EqualsAndHashCode
@ToString
public class NoFieldMismatch implements JsonMismatch, LocJsonMismatchFactory {
    private final JsonExpectation expectation;

    public NoFieldMismatch(JsonExpectation expectation) {
        this.expectation = checkNotNull(expectation);
    }
}
