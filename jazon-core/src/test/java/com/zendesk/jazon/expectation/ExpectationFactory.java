package com.zendesk.jazon.expectation;

import java.util.*;

import static java.util.stream.Collectors.*;

public interface ExpectationFactory {
    default JsonExpectation expectation(Object object) {
        return expectationKek(object).get();    //FIXME
    }
    Optional<JsonExpectation> expectationKek(Object object);

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
