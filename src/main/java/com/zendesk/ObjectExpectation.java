package com.zendesk;

import java.util.Map;
import java.util.Objects;

import static com.zendesk.JazonMatchResult.failure;
import static com.zendesk.JazonMatchResult.success;

public class ObjectExpectation implements JsonExpectation {
    private final Map<String, JsonExpectation> expectationMap;

    public ObjectExpectation(Map<String, JsonExpectation> expectationMap) {
        this.expectationMap = expectationMap;
    }

    @Override
    public JazonMatchResult match(ActualJsonNumber actualNumber) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonNumber.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonObject actualObject) {
        return matchMap(actualObject.map());
    }

    private JazonMatchResult matchMap(Map<String, Actual> jsonAsMap) {
        boolean sizesMatch = expectationMap.size() == jsonAsMap.size();
        if (!sizesMatch) {
            return failure(
                    new SizesJsonMismatch(expectationMap.size(), jsonAsMap.size())
            );
        }
        for (Map.Entry<String, JsonExpectation> entry : expectationMap.entrySet()) {
            // "what is expected" responsibility used here
            JsonExpectation fieldExpectation = entry.getValue();

            //FIXME below if-else need to ba a visitor call
            JazonMatchResult matchResult = null;
            Actual actual = jsonAsMap.get(entry.getKey());
            if (actual instanceof ActualJsonNumber) {
                matchResult = fieldExpectation.match((ActualJsonNumber) actual);
            } else if (actual instanceof ActualJsonString) {
                matchResult = fieldExpectation.match((ActualJsonString) actual);
            }

            if (!matchResult.ok()) {
                return failure(matchResult.mismatch());
            }
        }
        return success();
    }

    @Override
    public JazonMatchResult match(ActualJsonString actualString) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectExpectation that = (ObjectExpectation) o;
        return Objects.equals(expectationMap, that.expectationMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectationMap);
    }

    @Override
    public String toString() {
        return "ObjectExpectation{" +
                "expectationMap=" + expectationMap +
                '}';
    }
}
