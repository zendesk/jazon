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
    public MatchResult match(ActualJsonNumber actualNumber, String path) {
        return matchPrimitive(actualNumber.number(), ActualJsonNumber.class, path);
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject, String path) {
        return failure(
                new TypeMismatch(expectedJsonType, ActualJsonObject.class)
                        .at(path)
        );
    }

    @Override
    public MatchResult match(ActualJsonString actualString, String path) {
        return matchPrimitive(actualString.string(), ActualJsonString.class, path);
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull, String path) {
        return failure(
                new NullMismatch<>(expectedJsonType, expectedValue)
                        .at(path)
        );
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray, String path) {
        return failure(
                new TypeMismatch(expectedJsonType, ActualJsonArray.class)
                        .at(path)
        );
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean, String path) {
        return matchPrimitive(actualBoolean.value(), ActualJsonBoolean.class, path);
    }

    private <ActualType extends Actual> MatchResult matchPrimitive(Object actualValue,
                                                                   Class<ActualType> actualTypeClass,
                                                                   String path) {
        if (actualTypeClass != expectedJsonType) {
            return failure(
                    new TypeMismatch(expectedJsonType, actualTypeClass)
                            .at(path)
            );
        }
        if (expectedValue.equals(actualValue)) {
            return success();
        }
        return failure(
                new PrimitiveValueMismatch<>(expectedValue, actualValue)
                        .at(path)
        );
    }
}
