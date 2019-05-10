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
    public MatchResult match(ActualJsonNumber actualNumber, String path) {
        return predicate.test(actualNumber.number())
                ? success()
                : failure(PredicateMismatch.INSTANCE.at(path));
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject, String path) {
        return predicate.test(actualObject)
                ? success()
                : failure(PredicateMismatch.INSTANCE.at(path));
    }

    @Override
    public MatchResult match(ActualJsonString actualString, String path) {
        return predicate.test(actualString.string())
                ? success()
                : failure(PredicateMismatch.INSTANCE.at(path));
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull, String path) {
        return predicate.test(actualNull)
                ? success()
                : failure(PredicateMismatch.INSTANCE.at(path));
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray, String path) {
        return predicate.test(actualArray.list())
                ? success()
                : failure(PredicateMismatch.INSTANCE.at(path));
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean, String path) {
        return predicate.test(actualBoolean.value())
                ? success()
                : failure(PredicateMismatch.INSTANCE.at(path));
    }
}
