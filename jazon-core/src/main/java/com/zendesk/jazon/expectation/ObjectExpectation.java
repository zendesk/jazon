package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;
import static com.zendesk.jazon.MatchResult.failure;

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
        Optional<MismatchOccurrence> mismatchFromExpectedFields = mismatchFromExpectedFields(actualObject, path);
        Optional<MismatchOccurrence> mismatchFromUnexpected = mismatchFromUnexpected(actualObject, path);
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
        return failure(typeMismatch(ActualJsonBoolean.class, path));
    }

    @Override
    public String toString() {
        return partialJsonObject(2);
    }

    private String partialJsonObject(int firstFieldsCount) {
        if (expectationMap.isEmpty()) {
            return "{}";
        }
        String firstFields = expectationMap.entrySet().stream()
                .limit(firstFieldsCount)
                .map(e -> String.format("\"%s\": %s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", "));
        String suffix = firstFieldsCount < expectationMap.size() ? ", ...}" : "}";
        return "{" + firstFields + suffix;
    }

    private Optional<MismatchOccurrence> mismatchFromExpectedFields(ActualJsonObject actualObject, String path) {
        return new MismatchFactory(actualObject, path)
                .mismatchFromExpectedFields();
    }

    private Optional<MismatchOccurrence> mismatchFromUnexpected(ActualJsonObject actualObject, String path) {
        return new MismatchFactory(actualObject, path)
                .mismatchFromUnexpected();
    }

    private static <T> Optional<T> firstOf(Optional<T> first, Optional<T> second) {
        if (first.isPresent()) {
            return first;
        }
        return second;
    }

    private MismatchOccurrence typeMismatch(Class<? extends Actual> actualType, String path) {
        return new TypeMismatch(ActualJsonObject.class, actualType)
                .at(path);
    }

    @AllArgsConstructor
    private class MismatchFactory {
        private final ActualJsonObject actualObject;
        private final String path;

        Optional<MismatchOccurrence> mismatchFromExpectedFields() {
            return expectationMap.entrySet()
                    .stream()
                    .map(e -> matchResult(e.getKey(), e.getValue()))
                    .filter(matchResult -> !matchResult.ok())
                    .map(MatchResult::mismatch)
                    .findFirst();
        }

        private Optional<MismatchOccurrence> mismatchFromUnexpected() {
            Set<String> unexpectedFields = setsDifference(actualObject.keys(), expectationMap.keySet());
            return unexpectedFields.stream()
                    .map(fieldName ->
                            new UnexpectedFieldMismatch(fieldName)
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

        private Set<String> setsDifference(Set<String> first, Set<String> second) {
            HashSet<String> result = new HashSet<>();
            for (String memberOfFirst : first) {
                if (!second.contains(memberOfFirst)) {
                    result.add(memberOfFirst);
                }
            }
            return result;
        }
    }
}
