package com.zendesk.jazon.actual;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public interface ActualFactory {
    Actual actual(Object object);

    static ActualJsonObject actualObject(Map<String, Object> objectsMap, ActualFactory actualFactory) {
        Map<String, Actual> map = objectsMap.entrySet()
                .stream()
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                e -> actualFactory.actual(e.getValue())
                        )
                );
        return new ActualJsonObject(map);
    }

    static ActualJsonArray actualArray(List<Object> objects, ActualFactory actualFactory) {
        List<Actual> actuals = objects.stream()
                .map(actualFactory::actual)
                .collect(toList());
        return new ActualJsonArray(actuals);
    }
}
