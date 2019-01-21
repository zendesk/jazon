package com.zendesk.jazon.expectation;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.NullMismatch;
import com.zendesk.jazon.mismatch.TypeMismatch;

import java.util.List;
import java.util.Objects;

import static com.zendesk.jazon.JazonMatchResult.failure;
import static com.zendesk.jazon.JazonMatchResult.success;

class ListExpectation implements JsonExpectation {
    private final List<JsonExpectation> expectationList;

    ListExpectation(List<JsonExpectation> expectationList) {
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
        return success(); //TODO: implement seriously
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListExpectation that = (ListExpectation) o;
        return Objects.equals(expectationList, that.expectationList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectationList);
    }

    @Override
    public String toString() {
        return "ListExpectation{" +
                "expectationList=" + expectationList +
                '}';
    }

    private TypeMismatch typeMismatch(Class<? extends Actual> actualType) {
        return new TypeMismatch(ActualJsonArray.class, actualType);
    }
}
