package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;
import com.zendesk.jazon.mismatch.*;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zendesk.jazon.MatchResult.failure;
import static com.zendesk.jazon.MatchResult.success;
import static java.util.Arrays.asList;

/**
 * FIXME:
 * There are 2 problems with this implementation:
 *  1. `match(ActualJsonArray)` method has time complexity of O(n^2) which is high cost for large sets.
 *  2. Due to naive implementation of `match(ActualJsonArray)` method, expectation types that are not
 *      exact-equality-matches are not supported (currently the only such example is UnorderedArrayExpectation but
 *      soon expected CustomPredicateExpectation similarly will not be supported in UnorderedArrayExpectation).
 *      {@code SUPPORTED_EXPECTATION_TYPES} defines which types of expectation classes are supported.
 */
@EqualsAndHashCode
class UnorderedArrayExpectation implements JsonExpectation {
    private static final Set<Class<? extends JsonExpectation>> SUPPORTED_EXPECTATION_TYPES = new HashSet<>(asList(
            PrimitiveValueExpectation.class,
            ObjectExpectation.class,
            OrderedArrayExpectation.class
    ));
    private final Set<JsonExpectation> expectationSet;

    UnorderedArrayExpectation(Set<JsonExpectation> expectationSet) {
        expectationSet.forEach(this::verifyExpectationSupported);
        this.expectationSet = expectationSet;
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
        Set<JsonExpectation> stillExpected = new HashSet<>(expectationSet);
        ArrayList<Actual> actualList = new ArrayList<>(actualArray.list());

        for (JsonExpectation expectation : expectationSet) {
            for (int actualIndex = 0; actualIndex < actualList.size(); actualIndex++) {
                Actual actual = actualList.get(actualIndex);
                MatchResult result = actual.accept(expectation, path + ".?");
                if (result.ok()) {
                    actualList.remove(actual);
                    stillExpected.remove(expectation);
                    break;
                }
            }
        }

        if (!stillExpected.isEmpty()) {
            return failure(
                    new ArrayLackingElementsMismatch(stillExpected)
                            .at(path)
            );
        }
        if (!actualList.isEmpty()) {
            return failure(
                    new ArrayUnexpectedElementsMismatch(actualList)
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
        return "[" + String.join(", ", strings(expectationSet)) + "] (unordered)";
    }

    private MismatchWithPath typeMismatch(Class<? extends Actual> actualType, String path) {
        return new TypeMismatch(ActualJsonArray.class, actualType)
                .at(path);
    }

    private void verifyExpectationSupported(JsonExpectation expectation) {
        boolean isSupported = SUPPORTED_EXPECTATION_TYPES.contains(expectation.getClass());
        if (!isSupported) {
            throw new IllegalStateException(
                    String.format(
                            "%s is not supported in %s",
                            expectation.getClass(),
                            UnorderedArrayExpectation.class.toString()
                    )
            );
        }
    }

    private <T> Collection<String> strings(Collection<T> objects) {
        return objects.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }
}
