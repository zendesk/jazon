package com.zendesk.jazon.expectation;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.NullMismatch;
import com.zendesk.jazon.mismatch.PrimitiveValueMismatch;
import com.zendesk.jazon.mismatch.TypeMismatch;

import java.util.Objects;

import static com.zendesk.jazon.JazonMatchResult.failure;
import static com.zendesk.jazon.JazonMatchResult.success;

class PrimitiveValueExpectation<T> implements JsonExpectation {
    private final T expectedValue;
    private final Class<? extends Actual> expectedJsonType;

    PrimitiveValueExpectation(T expectedValue, Class<? extends Actual> expectedJsonType) {
        this.expectedValue = expectedValue;
        this.expectedJsonType = expectedJsonType;
    }

    @Override
    public JazonMatchResult match(ActualJsonNumber actualNumber) {
        return matchPrimitive(actualNumber.number(), ActualJsonNumber.class);
    }

    @Override
    public JazonMatchResult match(ActualJsonObject actualObject) {
        return failure(new TypeMismatch(expectedJsonType, ActualJsonObject.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonString actualString) {
        return matchPrimitive(actualString.string(), ActualJsonString.class);
    }

    @Override
    public JazonMatchResult match(ActualJsonNull actualNull) {
        return failure(new NullMismatch<>(expectedJsonType, expectedValue));
    }

    @Override
    public JazonMatchResult match(ActualJsonArray actualArray) {
        return failure(new TypeMismatch(expectedJsonType, ActualJsonArray.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonBoolean actualBoolean) {
        return matchPrimitive(actualBoolean.value(), ActualJsonBoolean.class);
    }

    private <ActualType extends Actual> JazonMatchResult matchPrimitive(Object actualValue, Class<ActualType> actualTypeClass) {
        if (actualTypeClass != expectedJsonType) {
            return failure(new TypeMismatch(expectedJsonType, actualTypeClass));
        }
        if (expectedValue.equals(actualValue)) {
            return success();
        }
        return failure(new PrimitiveValueMismatch<>(expectedValue, actualValue));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimitiveValueExpectation that = (PrimitiveValueExpectation) o;
        return Objects.equals(expectedValue, that.expectedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectedValue);
    }

    @Override
    public String toString() {
        return "PrimitiveValueExpectation{" +
                "expectedValue=" + expectedValue +
                '}';
    }
}
