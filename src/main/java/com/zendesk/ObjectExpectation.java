package com.zendesk;

import java.util.Map;

import static com.zendesk.JazonMatchResult.failure;
import static com.zendesk.JazonMatchResult.success;

public class ObjectExpectation implements JsonExpectationBeka {
    private final Map<String, JsonExpectationBeka> expectationMap;

    public ObjectExpectation(Map<String, JsonExpectationBeka> expectationMap) {
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
        for (Map.Entry<String, JsonExpectationBeka> entry : expectationMap.entrySet()) {
            // "what is expected" responsibility used here
            JsonExpectationBeka fieldExpectation = entry.getValue();
            // "what is actual" responsibility used here
            JazonMatchResult matchResult = fieldExpectation.match((ActualJsonNumber) jsonAsMap.get(entry.getKey()));
            if (!matchResult.ok()) {
                return failure(matchResult.mismatch());
            }
        }
        return success();
    }
}
