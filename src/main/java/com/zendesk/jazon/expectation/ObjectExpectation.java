package com.zendesk.jazon.expectation;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.JsonMismatch;
import com.zendesk.jazon.mismatch.NullMismatch;
import com.zendesk.jazon.mismatch.TypeMismatch;
import com.zendesk.jazon.mismatch.UnexpectedFieldMismatch;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.difference;
import static com.zendesk.jazon.JazonMatchResult.failure;

public class ObjectExpectation implements JsonExpectation {
    private final Map<String, JsonExpectation> expectationMap;

    public ObjectExpectation(Map<String, JsonExpectation> expectationMap) {
        this.expectationMap = expectationMap;
    }

    @Override
    public JazonMatchResult match(ActualJsonNumber actualNumber) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonNumber.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonObject actualObject) {
        Optional<JsonMismatch> mismatchFromExpectedFields = mismatchFromExpectedFields(actualObject);
        Optional<JsonMismatch> mismatchFromUnexpected = mismatchFromUnexpected(actualObject);
        return firstOf(mismatchFromExpectedFields, mismatchFromUnexpected)
                .map(JazonMatchResult::failure)
                .orElseGet(JazonMatchResult::success);
    }

    @Override
    public JazonMatchResult match(ActualJsonString actualString) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonString.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonNull actualNull) {
        return failure(new NullMismatch<>(ActualJsonObject.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonArray actualArray) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonArray.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonBoolean actualBoolean) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonBoolean.class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectExpectation that = (ObjectExpectation) o;
        return Objects.equals(expectationMap, that.expectationMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectationMap);
    }

    @Override
    public String toString() {
        return "ObjectExpectation{" +
                "expectationMap=" + expectationMap +
                '}';
    }

    private Optional<JsonMismatch> mismatchFromExpectedFields(ActualJsonObject actualObject) {
        return expectationMap.entrySet()
                .stream()
                .map(
                        e -> actual(actualObject, e.getKey())
                                .accept(e.getValue())
                )
                .filter(matchResult -> !matchResult.ok())
                .map(JazonMatchResult::mismatch)
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

    private Optional<JsonMismatch> firstOf(Optional<JsonMismatch> first, Optional<JsonMismatch> second) {
        if (first.isPresent()) {
            return first;
        }
        return second;
    }

    private Actual actual(ActualJsonObject jsonObject, String fieldName) {
        return jsonObject.actualField(fieldName)
                .orElseGet(ActualJsonNull::new);
    }
}
