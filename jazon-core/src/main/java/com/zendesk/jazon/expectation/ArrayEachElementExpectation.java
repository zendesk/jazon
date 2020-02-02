package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.MismatchWithPath;
import com.zendesk.jazon.mismatch.NullMismatch;
import com.zendesk.jazon.mismatch.TypeMismatch;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ListIterator;

import static com.zendesk.jazon.MatchResult.failure;
import static com.zendesk.jazon.MatchResult.success;

@RequiredArgsConstructor
@EqualsAndHashCode
class ArrayEachElementExpectation implements JsonExpectation {

    private final JsonExpectation expectationForEachElement;

    @Override
    public MatchResult match(ActualJsonNumber actualNumber, String path) {
        return failure(typeMismatch(ActualJsonNumber.class, path));
    }

    @Override
    public MatchResult match(ActualJsonObject actualObject, String path) {
        return failure(typeMismatch(ActualJsonObject.class, path));
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
        ListIterator<Actual> actualValues = actualArray.list().listIterator();
        while (actualValues.hasNext()) {
            Actual actualValue = actualValues.next();
            MatchResult matchResult = actualValue.accept(expectationForEachElement, path + "." + actualValues.previousIndex());
            if (!matchResult.ok()) {
                return matchResult;
            }
        }
        return success();
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean, String path) {
        return failure(typeMismatch(ActualJsonBoolean.class, path));
    }

    private MismatchWithPath typeMismatch(Class<? extends Actual> actualType, String path) {
        return new TypeMismatch(ActualJsonArray.class, actualType)
                .at(path);
    }
}
