package com.zendesk.jazon.expectation;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.NotNullMismatch;

import static com.zendesk.jazon.JazonMatchResult.failure;
import static com.zendesk.jazon.JazonMatchResult.success;

class NullExpectation implements JsonExpectation {
    @Override
    public JazonMatchResult match(ActualJsonNumber actualNumber) {
        return failure(new NotNullMismatch(actualNumber));
    }

    @Override
    public JazonMatchResult match(ActualJsonObject actualObject) {
        return failure(new NotNullMismatch(actualObject));
    }

    @Override
    public JazonMatchResult match(ActualJsonString actualString) {
        return failure(new NotNullMismatch(actualString));
    }

    @Override
    public JazonMatchResult match(ActualJsonNull actualNull) {
        return success();
    }

    @Override
    public JazonMatchResult match(ActualJsonArray actualArray) {
        return failure(new NotNullMismatch(actualArray));
    }

    @Override
    public JazonMatchResult match(ActualJsonBoolean actualBoolean) {
        return null;
    }
}
