package com.zendesk;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

final class ExpectationTranslators {
    private ExpectationTranslators() {
    }

    static ObjectExpectation objectExpectation(Map<String, Object> objectsMap) {
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
            return new ListExpectation((List<JsonExpectation>) object);
        }
        throw new IllegalArgumentException();
    }
}
