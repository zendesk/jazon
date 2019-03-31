package com.zendesk.jazon.expectation;

import java.util.*;

import static java.util.stream.Collectors.*;

public interface ExpectationFactory {
    JsonExpectation expectation(Object object);

    static ObjectExpectation objectExpectation(Map<String, Object> objectsMap, ExpectationFactory expectationFactory) {
        LinkedHashMap<String, JsonExpectation> expectationsMap = objectsMap.entrySet()
                .stream()
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                e -> expectationFactory.expectation(e.getValue()),
                                (a, b) -> a,
                                () -> new LinkedHashMap<>(objectsMap.size())
                        )
                );
        return new ObjectExpectation(expectationsMap);
    }

    static JsonExpectation expectedOrderedArray(List<Object> objectsList, ExpectationFactory expectationFactory) {
        List<JsonExpectation> expectations = objectsList.stream()
                .map(expectationFactory::expectation)
                .collect(toList());
        return new OrderedArrayExpectation(expectations);
    }

    static JsonExpectation expectedUnorderedArray(Set<Object> objectsSet, ExpectationFactory expectationFactory) {
        Set<JsonExpectation> expectations = objectsSet.stream()
                .map(expectationFactory::expectation)
                .collect(toSet());
        return new UnorderedArrayExpectation(expectations);
    }
}
