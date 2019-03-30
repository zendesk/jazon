package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.*;
import lombok.AllArgsConstructor;
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
    public MatchResult match(ActualJsonNumber actualNumber, String path) {
        return failure(typeMismatch(ActualJsonNumber.class, path));
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject, String path) {
        Optional<MismatchWithPath> mismatchFromExpectedFields = mismatchFromExpectedFields(actualObject, path);
        Optional<MismatchWithPath> mismatchFromUnexpected = mismatchFromUnexpected(actualObject, path);
        return firstOf(mismatchFromExpectedFields, mismatchFromUnexpected)
                .map(MatchResult::failure)
                .orElseGet(MatchResult::success);
    }

    @Override
    public MatchResult match(ActualJsonString actualString, String path) {
        return failure(typeMismatch(ActualJsonString.class, path));
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull, String path) {
        return failure(
                new NullMismatch<>(ActualJsonObject.class)
                        .at(path)
        );
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray, String path) {
        return failure(typeMismatch(ActualJsonArray.class, path));
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean, String path) {
        return failure(typeMismatch(ActualJsonBoolean.class, path));
    }

    private Optional<MismatchWithPath> mismatchFromExpectedFields(ActualJsonObject actualObject, String path) {
        return new MismatchFactory(actualObject, path)
                .mismatchFromExpectedFields();
    }

    private Optional<MismatchWithPath> mismatchFromUnexpected(ActualJsonObject actualObject, String path) {
        return new MismatchFactory(actualObject, path)
                .mismatchFromUnexpected();
    }

    private static <T> Optional<T> firstOf(Optional<T> first, Optional<T> second) {
        if (first.isPresent()) {
            return first;
        }
        return second;
    }

    private MismatchWithPath typeMismatch(Class<? extends Actual> actualType, String path) {
        return new TypeMismatch(ActualJsonObject.class, actualType)
                .at(path);
    }

    @AllArgsConstructor
    private class MismatchFactory {
        private final ActualJsonObject actualObject;
        private final String path;

        Optional<MismatchWithPath> mismatchFromExpectedFields() {
            return expectationMap.entrySet()
                    .stream()
                    .map(e -> matchResult(e.getKey(), e.getValue()))
                    .filter(matchResult -> !matchResult.ok())
                    .map(MatchResult::mismatch)
                    .findFirst();
        }

        private Optional<MismatchWithPath> mismatchFromUnexpected() {
            Set<String> unexpectedFields = difference(actualObject.keys(), expectationMap.keySet());
            return unexpectedFields.stream()
                    .map(actualObject::actualPresentField)
                    .map(actualField ->
                            new UnexpectedFieldMismatch<>(actualField.getClass())
                                    .at(path)
                    )
                    .findFirst();
        }

        private MatchResult matchResult(String fieldName, JsonExpectation expectation) {
            return actualObject.actualField(fieldName)
                    .map(actual -> actual.accept(expectation, path + "." + fieldName))
                    .orElseGet(() ->
                            failure(
                                    new NoFieldMismatch(fieldName, expectation)
                                            .at(path)
                            )
                    );
        }
    }
}
