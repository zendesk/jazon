package com.zendesk.jazon.junit;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * This class exists to allow to pass a lambda-predicate to the same interface as other typical objects like String,
 * Integer, List, etc. are passed. This is due to the limitation that {@code Object} is not effectively a supertype of
 * lambda expression.
 */
@EqualsAndHashCode
public class JazonMap {
    private final Map<String, JsonExpectationInput> map = new HashMap<>();

    public JazonMap with(String fieldName, Object fieldValue) {
        map.put(fieldName, new ObjectExpectationInput(fieldValue));
        return this;
    }

    public <T> JazonMap with(String fieldName, Predicate<T> predicate) {
        map.put(fieldName, new PredicateExpectationInput<>(predicate));
        return this;
    }

    public Map<String, JsonExpectationInput> map() {
        return map;
    }
}
