package com.zendesk.jazon.expectation;

import com.google.common.collect.ImmutableSet;
import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.ArrayLackingElementsMismatch;
import com.zendesk.jazon.mismatch.ArrayUnexpectedElementsMismatch;
import com.zendesk.jazon.mismatch.NullMismatch;
import com.zendesk.jazon.mismatch.TypeMismatch;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;
import static com.zendesk.jazon.JazonMatchResult.failure;
import static com.zendesk.jazon.JazonMatchResult.success;

/**
 * FIXME:
 * There are 2 problems with this implementation:
 *  1. `match(ActualJsonArray)` method has time complexity of O(n^2) which is high cost for large sets.
 *  2. Due to naive implementation of `match(ActualJsonArray)` method, expectation types that are not
 *      exact-equality-matches are not supported (currently the only such example is UnorderedArrayExpectation but
 *      soon expected CustomPredicateExpectation similarly will not be supported in UnorderedArrayExpectation).
 *      {@code SUPPORTED_EXPECTATION_TYPES} defines which types of expectation classes are supported.
 */
class UnorderedArrayExpectation implements JsonExpectation {
    private static final Set<Class<? extends JsonExpectation>> SUPPORTED_EXPECTATION_TYPES = ImmutableSet.of(
            PrimitiveValueExpectation.class,
            ObjectExpectation.class,
            OrderedArrayExpectation.class
    );
    private final Set<JsonExpectation> expectationSet;

    UnorderedArrayExpectation(Set<JsonExpectation> expectationSet) {
        expectationSet.forEach(this::verifyExpectationSupported);
        this.expectationSet = expectationSet;
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
        ArrayList<JsonExpectation> expectedButNotFound = new ArrayList<>();
        ArrayList<Actual> actualList = new ArrayList<>(actualArray.list());

        for (JsonExpectation expectation : expectationSet) {
            for (int actualIndex = 0; actualIndex < actualList.size(); actualIndex++) {
                Actual actual = actualList.get(actualIndex);
                JazonMatchResult result = actual.accept(expectation);
                if (result.ok()) {
                    actualList.remove(actual);
                    break;
                } else if (actualIndex == actualList.size() - 1) {
                    expectedButNotFound.add(expectation);
                }
            }
        }

        if (!expectedButNotFound.isEmpty()) {
            return failure(new ArrayLackingElementsMismatch(expectedButNotFound));
        }
        if (!actualList.isEmpty()) {
            return failure(new ArrayUnexpectedElementsMismatch(actualList));
        }
        return success();
    }

    @Override
    public JazonMatchResult match(ActualJsonBoolean actualBoolean) {
        return null;
    }

    @Override
    public String toString() {
        return "UnorderedArrayExpectation{" +
                "expectationSet=" + expectationSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnorderedArrayExpectation that = (UnorderedArrayExpectation) o;
        return Objects.equals(expectationSet, that.expectationSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectationSet);
    }

    private TypeMismatch typeMismatch(Class<? extends Actual> actualType) {
        return new TypeMismatch(ActualJsonArray.class, actualType);
    }

    private void verifyExpectationSupported(JsonExpectation expectation) {
        boolean isSupported = SUPPORTED_EXPECTATION_TYPES.contains(expectation.getClass());
        checkState(isSupported,
                "{} is not supported in {}",
                expectation.getClass(),
                UnorderedArrayExpectation.class.toString()
        );
    }
}
