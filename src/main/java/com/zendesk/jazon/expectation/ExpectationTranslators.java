package com.zendesk.jazon.expectation;

import com.zendesk.jazon.actual.ActualJsonBoolean;
import com.zendesk.jazon.actual.ActualJsonNumber;
import com.zendesk.jazon.actual.ActualJsonString;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;

public final class ExpectationTranslators {
    private ExpectationTranslators() {
    }

    public static ObjectExpectation objectExpectation(Map<String, Object> objectsMap) {
        Map<String, JsonExpectation> expectationsMap = objectsMap.entrySet()
                .stream()
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                e -> expectation(e.getValue())
                        )
                );
        return new ObjectExpectation(expectationsMap);
    }

    private static JsonExpectation expectation(Object object) {
        // TODO make this customisable+overridable with configuration
        if (object instanceof Map) {
            return objectExpectation((Map<String, Object>) object);
        } else if (object instanceof Number) {
            return new PrimitiveValueExpectation<>((Number) object, ActualJsonNumber.class);
        } else if (object instanceof String) {
            return new PrimitiveValueExpectation<>((String) object, ActualJsonString.class);
        } else if (object instanceof Boolean) {
            return new PrimitiveValueExpectation<>((Boolean) object, ActualJsonBoolean.class);
        } else if (object instanceof List) {
            return expectedOrderedArray((List<Object>) object);
        } else if (object instanceof Set) {
            return expectedUnorderedArray((Set<Object>) object);
        } else if (object == null) {
            return new NullExpectation();
        }
        throw new IllegalArgumentException();
    }

    private static JsonExpectation expectedOrderedArray(List<Object> objectsList) {
        List<JsonExpectation> expectations = objectsList.stream()
                .map(ExpectationTranslators::expectation)
                .collect(toList());
        return new OrderedArrayExpectation(expectations);
    }

    private static JsonExpectation expectedUnorderedArray(Set<Object> objectsSet) {
        Set<JsonExpectation> expectations = objectsSet.stream()
                .map(ExpectationTranslators::expectation)
                .collect(toSet());
        return new UnorderedArrayExpectation(expectations);
    }
}
