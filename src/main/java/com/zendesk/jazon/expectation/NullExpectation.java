package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.NotNullMismatch;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.zendesk.jazon.MatchResult.failure;
import static com.zendesk.jazon.MatchResult.success;

@ToString
@EqualsAndHashCode
class NullExpectation implements JsonExpectation {
    @Override
    public MatchResult match(ActualJsonNumber actualNumber) {
        return failure(new NotNullMismatch(actualNumber));
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject) {
        return failure(new NotNullMismatch(actualObject));
    }

    @Override
    public MatchResult match(ActualJsonString actualString) {
        return failure(new NotNullMismatch(actualString));
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull) {
        return success();
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray) {
        return failure(new NotNullMismatch(actualArray));
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean) {
        return null;
    }
}
