package com.zendesk.jazon.expectation;

import com.zendesk.jazon.actual.*;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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
        } else if (object instanceof List) {
            return expectedOrderedArray((List<Object>) object);
        }
        throw new IllegalArgumentException();
    }

    private static JsonExpectation expectedOrderedArray(List<Object> objectsList) {
        List<JsonExpectation> expectations = objectsList.stream()
                .map(ExpectationTranslators::expectation)
                .collect(toList());
        return new OrderedArrayExpectation(expectations);
    }
}
