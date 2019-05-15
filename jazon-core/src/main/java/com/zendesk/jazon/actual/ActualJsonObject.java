package com.zendesk.jazon.actual;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
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
    public MatchResult accept(JsonExpectation expectation, String path) {
        return expectation.match(this, path);
    }
}
