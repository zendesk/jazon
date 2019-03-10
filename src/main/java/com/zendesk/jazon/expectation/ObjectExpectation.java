package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.JsonMismatch;
import com.zendesk.jazon.mismatch.NullMismatch;
import com.zendesk.jazon.mismatch.TypeMismatch;
import com.zendesk.jazon.mismatch.UnexpectedFieldMismatch;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.difference;
import static com.zendesk.jazon.MatchResult.failure;

@ToString
@EqualsAndHashCode
public class ObjectExpectation implements JsonExpectation {
    private final Map<String, JsonExpectation> expectationMap;

    public ObjectExpectation(Map<String, JsonExpectation> expectationMap) {
        this.expectationMap = checkNotNull(expectationMap);
    }

    @Override
    public MatchResult match(ActualJsonNumber actualNumber) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonNumber.class));
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject) {
        Optional<JsonMismatch> mismatchFromExpectedFields = mismatchFromExpectedFields(actualObject);
        Optional<JsonMismatch> mismatchFromUnexpected = mismatchFromUnexpected(actualObject);
        return firstOf(mismatchFromExpectedFields, mismatchFromUnexpected)
                .map(MatchResult::failure)
                .orElseGet(MatchResult::success);
    }

    @Override
    public MatchResult match(ActualJsonString actualString) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonString.class));
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull) {
        return failure(new NullMismatch<>(ActualJsonObject.class));
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonArray.class));
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonBoolean.class));
    }

    private Optional<JsonMismatch> mismatchFromExpectedFields(ActualJsonObject actualObject) {
        return expectationMap.entrySet()
                .stream()
                .map(
                        e -> actual(actualObject, e.getKey())
                                .accept(e.getValue())
                )
                .filter(matchResult -> !matchResult.ok())
                .map(MatchResult::mismatch)
                .findFirst();
    }

    private Optional<JsonMismatch> mismatchFromUnexpected(ActualJsonObject actualObject) {
        Set<String> unexpectedFields = difference(actualObject.keys(), expectationMap.keySet());
        return unexpectedFields.stream()
                .map(actualObject::actualPresentField)
                .map(actualField -> new UnexpectedFieldMismatch<>(actualField.getClass()))
                .map(JsonMismatch.class::cast)
                .findFirst();
    }

    private static Optional<JsonMismatch> firstOf(Optional<JsonMismatch> first, Optional<JsonMismatch> second) {
        if (first.isPresent()) {
            return first;
        }
        return second;
    }

    private Actual actual(ActualJsonObject jsonObject, String fieldName) {
        return jsonObject.actualField(fieldName)
                .orElse(ActualJsonNull.INSTANCE);
    }
}
