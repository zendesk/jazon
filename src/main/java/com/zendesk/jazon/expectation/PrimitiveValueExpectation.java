package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.NullMismatch;
import com.zendesk.jazon.mismatch.PrimitiveValueMismatch;
import com.zendesk.jazon.mismatch.TypeMismatch;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.zendesk.jazon.MatchResult.failure;
import static com.zendesk.jazon.MatchResult.success;

@ToString
@EqualsAndHashCode
class PrimitiveValueExpectation<T> implements JsonExpectation {
    private final T expectedValue;
    private final Class<? extends Actual> expectedJsonType;

    PrimitiveValueExpectation(T expectedValue, Class<? extends Actual> expectedJsonType) {
        this.expectedValue = checkNotNull(expectedValue);
        this.expectedJsonType = checkNotNull(expectedJsonType);
    }

    @Override
    public MatchResult match(ActualJsonNumber actualNumber) {
        return matchPrimitive(actualNumber.number(), ActualJsonNumber.class);
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject) {
        return failure(new TypeMismatch(expectedJsonType, ActualJsonObject.class));
    }

    @Override
    public MatchResult match(ActualJsonString actualString) {
        return matchPrimitive(actualString.string(), ActualJsonString.class);
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull) {
        return failure(new NullMismatch<>(expectedJsonType, expectedValue));
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray) {
        return failure(new TypeMismatch(expectedJsonType, ActualJsonArray.class));
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean) {
        return matchPrimitive(actualBoolean.value(), ActualJsonBoolean.class);
    }

    private <ActualType extends Actual> MatchResult matchPrimitive(Object actualValue, Class<ActualType> actualTypeClass) {
        if (actualTypeClass != expectedJsonType) {
            return failure(new TypeMismatch(expectedJsonType, actualTypeClass));
        }
        if (expectedValue.equals(actualValue)) {
            return success();
        }
        return failure(new PrimitiveValueMismatch<>(expectedValue, actualValue));
    }
}
