package com.zendesk.jazon.expectation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public interface ExpectationFactory {
    default JsonExpectation expectation(Object object) {
        return expectationMaybe(object).get();
    }

    Optional<JsonExpectation> expectationMaybe(Object object);

    static ObjectExpectation objectExpectation(Map<CharSequence, Object> objectsMap, ExpectationFactory expectationFactory) {
        LinkedHashMap<String, JsonExpectation> expectationsMap = objectsMap.entrySet()
                .stream()
                .collect(
                        toMap(
                                e -> e.getKey().toString(),
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
