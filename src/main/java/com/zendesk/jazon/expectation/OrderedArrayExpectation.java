package com.zendesk.jazon.expectation;

import com.google.common.collect.Iterators;
import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.zendesk.jazon.JazonMatchResult.failure;
import static com.zendesk.jazon.JazonMatchResult.success;

class OrderedArrayExpectation implements JsonExpectation {
    private final List<JsonExpectation> expectationList;

    OrderedArrayExpectation(List<JsonExpectation> expectationList) {
        this.expectationList = expectationList;
    }

    @Override
    public JazonMatchResult match(ActualJsonNumber actualNumber) {
        return failure(typeMismatch(ActualJsonNumber.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonObject actualObject) {
        return failure(typeMismatch(ActualJsonObject.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonString actualString) {
        return failure(typeMismatch(ActualJsonString.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonNull actualNull) {
        return failure(new NullMismatch<>(ActualJsonArray.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonArray actualArray) {
        int index = 0;
        Iterator<JsonExpectation> expectationIterator = expectationList.iterator();
        Iterator<Actual> actualIterator = actualArray.list().iterator();

        while (expectationIterator.hasNext() && actualIterator.hasNext()) {
            JsonExpectation expectation = expectationIterator.next();
            Actual actual = actualIterator.next();
            JazonMatchResult matchResult = actual.accept(expectation);
            if (!matchResult.ok()) {
                return failure(new ArrayElementMismatch(index, matchResult.mismatch()));
            }
            index += 1;
        }

        if (expectationIterator.hasNext()) {
            List<JsonExpectation> lackingElements = remainingItems(expectationIterator);
            return failure(new ArrayLackingElementsMismatch(lackingElements));
        }

        if (actualIterator.hasNext()) {
            List<Actual> unexpectedElements = remainingItems(actualIterator);
            return failure(new ArrayUnexpectedElementsMismatch(unexpectedElements));
        }

        return success();
    }

    @Override
    public JazonMatchResult match(ActualJsonBoolean actualBoolean) {
        return null;
    }

    private <T> List<T> remainingItems(Iterator<T> iterator) {
        ArrayList<T> result = new ArrayList<>();
        Iterators.addAll(result, iterator);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedArrayExpectation that = (OrderedArrayExpectation) o;
        return Objects.equals(expectationList, that.expectationList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectationList);
    }

    @Override
    public String toString() {
        return "OrderedArrayExpectation{" +
                "expectationList=" + expectationList +
                '}';
    }

    private TypeMismatch typeMismatch(Class<? extends Actual> actualType) {
        return new TypeMismatch(ActualJsonArray.class, actualType);
    }
}
