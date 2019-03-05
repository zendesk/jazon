package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.PredicateMismatch;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.zendesk.jazon.MatchResult.failure;
import static com.zendesk.jazon.MatchResult.success;

@ToString
@EqualsAndHashCode
class PredicateExpectation implements JsonExpectation {
    private final Predicate<Object> predicate;

    PredicateExpectation(Predicate<Object> predicate) {
        this.predicate = checkNotNull(predicate);
    }

    @Override
    public MatchResult match(ActualJsonNumber actualNumber) {
        return predicate.test(actualNumber.number())
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject) {
        return predicate.test(actualObject)
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public MatchResult match(ActualJsonString actualString) {
        return predicate.test(actualString.string())
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull) {
        return predicate.test(actualNull)
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray) {
        return predicate.test(actualArray)
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean) {
        return predicate.test(actualBoolean.value())
                ? success()
                : failure(PredicateMismatch.INSTANCE);
    }
}
