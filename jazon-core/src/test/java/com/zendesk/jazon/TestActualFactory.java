package com.zendesk.jazon;

import com.zendesk.jazon.actual.Actual;
import com.zendesk.jazon.actual.ActualFactory;
import com.zendesk.jazon.actual.ActualJsonArray;
import com.zendesk.jazon.actual.ActualJsonBoolean;
import com.zendesk.jazon.actual.ActualJsonNull;
import com.zendesk.jazon.actual.ActualJsonNumber;
import com.zendesk.jazon.actual.ActualJsonObject;
import com.zendesk.jazon.actual.ActualJsonString;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * This class exists to handle assertions in tests when instance of Actual is needed in the assertion-expectation - e.g.
 * equality-comparing of `Mismatch` instances that store Actual instance inside itself.
 * Without this class we would have to write lot of verbose constructor-calls like `new ActualJsonArray(...)` etc.
 */
public class TestActualFactory implements ActualFactory<Object> {

    @Override
    public Actual actual(Object input) {
        if (input instanceof Map) {
            return actualObject((Map<String, Object>) input);
        } else if (input instanceof Number) {

            return new ActualJsonNumber((Number) input);
        } else if (input instanceof String) {
            return new ActualJsonString((String) input);
        } else if (input == null) {
            return ActualJsonNull.INSTANCE;
        } else if (input instanceof List) {
            return actualArray((List<Object>) input);
        } else if (input instanceof Boolean) {
            return new ActualJsonBoolean((Boolean) input);
        }
        throw new IllegalArgumentException();
    }

    private ActualJsonObject actualObject(Map<String, Object> objectsMap) {
        Map<String, Actual> map = objectsMap.entrySet()
                .stream()
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                e -> actual(e.getValue()),
                                (a, b) -> a,
                                LinkedHashMap::new
                        )
                );
        return new ActualJsonObject(map);
    }

    private ActualJsonArray actualArray(List<Object> objects) {
        List<Actual> actuals = objects.stream()
                .map(this::actual)
                .collect(toList());
        return new ActualJsonArray(actuals);
    }
}
