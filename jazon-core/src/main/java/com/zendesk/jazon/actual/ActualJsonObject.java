package com.zendesk.jazon.actual;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;
import static java.util.Collections.unmodifiableMap;

@EqualsAndHashCode
public class ActualJsonObject implements Actual {
    private final Map<String, Actual> map;

    ActualJsonObject(Map<String, Actual> map) {
        this.map = checkNotNull(map);
    }

    public Optional<Actual> actualField(String fieldName) {
        return Optional.ofNullable(map.get(fieldName));
    }

    public Map<String, Actual> map() {
        return unmodifiableMap(map);
    }

    public Set<String> keys() {
        return map.keySet();
    }

    @Override
    public MatchResult accept(JsonExpectation expectation, String path) {
        return expectation.match(this, path);
    }

    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        if (map.isEmpty()) {
            return "{}";
        }
        return "{" + fields() + "}";
    }

    private String fields() {
        return map.entrySet().stream()
                .map(e -> String.format("\"%s\": %s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", "));
    }
}
