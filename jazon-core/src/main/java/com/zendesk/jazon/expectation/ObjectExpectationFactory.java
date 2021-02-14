package com.zendesk.jazon.expectation;

import lombok.AllArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
public class ObjectExpectationFactory {
    private final ExpectationFactory expectationFactory;

    public ObjectExpectation objectExpectation(Map<CharSequence, Object> objectsMap) {
        LinkedHashMap<String, JsonExpectation> expectationsMap = objectsMap.entrySet()
                .stream()
                .collect(
                        toMap(
                                e -> e.getKey().toString(),
                                e -> translated(e.getValue()),
                                (a, b) -> a,
                                () -> new LinkedHashMap<>(objectsMap.size())
                        )
                );
        return new ObjectExpectation(expectationsMap);
    }

    private JsonExpectation translated(Object object) {
        return expectationFactory.expectation(object);
    }
}
