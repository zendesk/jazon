package com.zendesk.jazon.expectation.impl;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.expectation.JsonExpectation;
import com.zendesk.jazon.mismatch.MismatchWithPath;
import com.zendesk.jazon.mismatch.NullMismatch;
import com.zendesk.jazon.mismatch.PrimitiveValueMismatch;
import com.zendesk.jazon.mismatch.TypeMismatch;
import lombok.EqualsAndHashCode;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;
import static com.zendesk.jazon.MatchResult.failure;
import static com.zendesk.jazon.MatchResult.success;

@EqualsAndHashCode
public class PrimitiveValueExpectation<T extends Actual> implements JsonExpectation {
    private final T expectedValue;

    public PrimitiveValueExpectation(T expectedValue) {
        this.expectedValue = checkNotNull(expectedValue);
    }

    @Override
    public MatchResult match(ActualJsonNumber actualNumber, String path) {
        return matchPrimitive(actualNumber, path);
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject, String path) {
        return failure(typeMismatch(ActualJsonObject.class, path));
    }

    @Override
    public MatchResult match(ActualJsonString actualString, String path) {
        return matchPrimitive(actualString, path);
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull, String path) {
        return failure(
                new NullMismatch<>(this)
                        .at(path)
        );
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray, String path) {
        return failure(typeMismatch(ActualJsonArray.class, path));
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean, String path) {
        return matchPrimitive(actualBoolean, path);
    }

    @Override
    public String toString() {
        return expectedValue.toString();
    }

    private <ActualType extends Actual> MatchResult matchPrimitive(ActualType actualValue, String path) {
        if (actualValue.getClass() != expectedType()) {
            return failure(typeMismatch(actualValue.getClass(), path));
        }
        if (expectedValue.equals(actualValue)) {
            return success();
        }
        return failure(
                new PrimitiveValueMismatch<>(expectedValue, actualValue)
                        .at(path)
        );
    }

    private MismatchWithPath typeMismatch(Class<? extends Actual> actualType, String path) {
        return new TypeMismatch(expectedType(), actualType)
                .at(path);
    }

    private Class<? extends Actual> expectedType() {
        return expectedValue.getClass();
    }
}
