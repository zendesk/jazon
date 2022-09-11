package com.zendesk.jazon.expectation.impl;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.expectation.JsonExpectation;
import com.zendesk.jazon.mismatch.*;
import com.zendesk.jazon.mismatch.impl.ArrayLackingElementsMismatch;
import com.zendesk.jazon.mismatch.impl.ArrayUnexpectedElementsMismatch;
import com.zendesk.jazon.mismatch.impl.NullMismatch;
import com.zendesk.jazon.mismatch.impl.TypeMismatch;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;
import static com.zendesk.jazon.MatchResult.failure;
import static com.zendesk.jazon.MatchResult.success;

@EqualsAndHashCode
public class OrderedArrayExpectation implements JsonExpectation {
    private final List<JsonExpectation> expectationList;

    public OrderedArrayExpectation(List<JsonExpectation> expectationList) {
        this.expectationList = checkNotNull(expectationList);
    }

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
        int index = 0;
        Iterator<JsonExpectation> expectationIterator = expectationList.iterator();
        Iterator<Actual> actualIterator = actualArray.list().iterator();

        while (expectationIterator.hasNext() && actualIterator.hasNext()) {
            JsonExpectation expectation = expectationIterator.next();
            Actual actual = actualIterator.next();
            MatchResult matchResult = actual.accept(expectation, path + "." + index);
            if (!matchResult.ok()) {
                return matchResult;
            }
            index += 1;
        }

        if (expectationIterator.hasNext()) {
            List<JsonExpectation> lackingElements = remainingItems(expectationIterator);
            return failure(
                    new ArrayLackingElementsMismatch(lackingElements)
                            .at(path)
            );
        }

        if (actualIterator.hasNext()) {
            List<Actual> unexpectedElements = remainingItems(actualIterator);
            return failure(
                    new ArrayUnexpectedElementsMismatch(unexpectedElements)
                            .at(path)
            );
        }

        return success();
    }

    @Override
    public MatchResult match(ActualJsonBoolean actualBoolean, String path) {
        return failure(typeMismatch(ActualJsonBoolean.class, path));
    }

    @Override
    public String toString() {
        return "[" + String.join(", ", strings(expectationList)) + "]";
    }

    private <T> List<T> remainingItems(Iterator<T> iterator) {
        ArrayList<T> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    private MismatchWithPath typeMismatch(Class<? extends Actual> actualType, String path) {
        return new TypeMismatch(ActualJsonArray.class, actualType)
                .at(path);
    }

    private <T> Collection<String> strings(Collection<T> objects) {
        return objects.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }
}
