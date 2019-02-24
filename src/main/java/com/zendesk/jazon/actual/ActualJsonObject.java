package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActualJsonObject implements Actual {
    private final Map<String, Actual> map;

    ActualJsonObject(Map<String, Actual> map) {
        this.map = checkNotNull(map);
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

    @Override
    public String toString() {
        return "ActualJsonObject{" +
                "map=" + map +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActualJsonObject that = (ActualJsonObject) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
