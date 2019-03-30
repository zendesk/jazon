package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.MismatchWithPath;
import com.zendesk.jazon.mismatch.NotNullMismatch;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.zendesk.jazon.MatchResult.failure;
import static com.zendesk.jazon.MatchResult.success;

@ToString
@EqualsAndHashCode
class NullExpectation implements JsonExpectation {
    @Override
    public MatchResult match(ActualJsonNumber actualNumber, String path) {
        return failure(notNullMismatch(actualNumber, path));
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject, String path) {
        return failure(notNullMismatch(actualObject, path));
    }

    @Override
    public MatchResult match(ActualJsonString actualString, String path) {
        return failure(notNullMismatch(actualString, path));
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull, String path) {
        return success();
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray, String path) {
        return failure(notNullMismatch(actualArray, path));
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean, String path) {
        return null;
    } //TODO!!!!!

    private MismatchWithPath notNullMismatch(Actual actual, String path) {
        return new NotNullMismatch(actual)
                .at(path);
    }
}
