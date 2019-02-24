package com.zendesk.jazon.expectation;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.PredicateMismatch;

import java.util.function.Predicate;

import static com.zendesk.jazon.JazonMatchResult.failure;
import static com.zendesk.jazon.JazonMatchResult.success;

class PredicateExpectation implements JsonExpectation {
    private final Predicate<Object> predicate;

    PredicateExpectation(Predicate<Object> predicate) {
        this.predicate = predicate;
    }

    @Override
    public JazonMatchResult match(ActualJsonNumber actualNumber) {
        return predicate.test(actualNumber.number())
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public JazonMatchResult match(ActualJsonObject actualObject) {
        return predicate.test(actualObject)
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public JazonMatchResult match(ActualJsonString actualString) {
        return predicate.test(actualString.string())
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public JazonMatchResult match(ActualJsonNull actualNull) {
        return predicate.test(actualNull)
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public JazonMatchResult match(ActualJsonArray actualArray) {
        return predicate.test(actualArray)
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public JazonMatchResult match(ActualJsonBoolean actualBoolean) {
        return predicate.test(actualBoolean.value())
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }
}
