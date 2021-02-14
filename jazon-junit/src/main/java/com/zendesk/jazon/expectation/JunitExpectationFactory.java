package com.zendesk.jazon.expectation;

import com.zendesk.jazon.junit.JazonMap;
import com.zendesk.jazon.junit.JsonExpectationInput;
import com.zendesk.jazon.junit.ObjectExpectationInput;
import com.zendesk.jazon.junit.PredicateExpectationInput;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toMap;

public class JunitExpectationFactory implements ExpectationFactory {

    @Override
    public Optional<JsonExpectation> expectationKek(Object object) {
        if (object instanceof ObjectExpectationInput) {
            ObjectExpectationInput expectationInput = (ObjectExpectationInput) object;
            return of(expectation(expectationInput.object()));
        } else if (object instanceof PredicateExpectationInput) {
            PredicateExpectationInput expectationInput = (PredicateExpectationInput) object;
            return of(new PredicateExpectation(expectationInput.predicate()));
        } else if (object instanceof JazonMap) {
            JazonMap jazonMap = (JazonMap) object;
            HashMap<CharSequence, Object> map = copied(jazonMap.map());
            return of(ExpectationFactory.objectExpectation(map, this));
        }
        return Optional.empty();
    }

    /**
     * Returns Map<CharSequence, Object> converted from Map<String, JsonExpectationInput>
     */
    private HashMap<CharSequence, Object> copied(Map<String, JsonExpectationInput> map) {
        return map
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));
    }
}
