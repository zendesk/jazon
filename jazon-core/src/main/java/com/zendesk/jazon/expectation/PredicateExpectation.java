package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.PredicateMismatch;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.zendesk.jazon.MatchResult.failure;
import static com.zendesk.jazon.MatchResult.success;
import static com.zendesk.jazon.util.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

@ToString
@EqualsAndHashCode
class PredicateExpectation implements JsonExpectation {
    private final Predicate<Object> predicate;

    PredicateExpectation(Predicate<Object> predicate) {
        this.predicate = checkNotNull(predicate);
    }

    @Override
    public MatchResult match(ActualJsonNumber actualNumber, String path) {
        return matchUnwrapped(actualNumber, path);
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject, String path) {
        return matchUnwrapped(actualObject, path);
    }

    @Override
    public MatchResult match(ActualJsonString actualString, String path) {
        return matchUnwrapped(actualString, path);
    }

    @Override
    public MatchResult match(ActualJsonNull actualNull, String path) {
        return matchUnwrapped(actualNull, path);
    }

    @Override
    public MatchResult match(ActualJsonArray actualArray, String path) {
        return matchUnwrapped(actualArray, path);
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean, String path) {
        return matchUnwrapped(actualBoolean, path);
    }

    private MatchResult matchUnwrapped(Actual actual, String path) {
        return predicate.test(unwrap(actual))
                ? success()
                : failure(PredicateMismatch.INSTANCE.at(path));
    }

    private Map<String, Object> unwrapObject(ActualJsonObject actualObject) {
        Map<String, Object> resultMap = new HashMap<>(actualObject.size());
        for (Map.Entry<String, Actual> entry : actualObject.map().entrySet()) {
            resultMap.put(entry.getKey(), unwrap(entry.getValue()));
        }
        return resultMap;
    }

    private List<Object> unwrapArray(ActualJsonArray actualJsonArray) {
        return actualJsonArray.list().stream()
                .map(this::unwrap)
                .collect(toList());
    }

    private Object unwrap(Actual actual) {
        if (actual instanceof ActualJsonString) {
            return ((ActualJsonString) actual).string();
        } else if (actual instanceof ActualJsonNumber) {
            return ((ActualJsonNumber) actual).number();
        } else if (actual instanceof ActualJsonBoolean) {
            return ((ActualJsonBoolean) actual).value();
        } else if (actual instanceof ActualJsonNull) {
            return null;
        } else if (actual instanceof ActualJsonObject) {
            return unwrapObject((ActualJsonObject) actual);
        } else if (actual instanceof ActualJsonArray) {
            return unwrapArray((ActualJsonArray) actual);
        }
        throw new IllegalArgumentException("Not a valid Actual object: " + actual);
    }
}
