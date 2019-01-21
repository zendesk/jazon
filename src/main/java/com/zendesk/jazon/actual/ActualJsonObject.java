package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ActualJsonObject implements Actual {
    private final Map<String, Actual> map;

    ActualJsonObject(Map<String, Actual> map) {
        this.map = map;
    }

    public Optional<Actual> actualField(String fieldName) {
        return Optional.ofNullable(map.get(fieldName));
    }

    public Actual actualPresentField(String fieldName) {
        if (map.containsKey(fieldName)) {
            return map.get(fieldName);
        }
        throw new IllegalStateException("Field " + fieldName +
                " not found. To use this method you have to be sure the field exists.");
    }

    public Set<String> keys() {
        return map.keySet();
    }

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }
}
