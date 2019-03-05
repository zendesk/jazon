package com.zendesk.jazon.actual;

import java.util.List;
import java.util.Map;

import static com.zendesk.jazon.actual.ActualFactory.actualArray;
import static com.zendesk.jazon.actual.ActualFactory.actualObject;

class DefaultActualFactory implements ActualFactory {

    @Override
    public Actual actual(Object object) {
        if (object instanceof Map) {
            return actualObject((Map<String, Object>) object, this);
        } else if (object instanceof Number) {
            return new ActualJsonNumber((Number) object);
        } else if (object instanceof String) {
            return new ActualJsonString((String) object);
        } else if (object == null) {
            return ActualJsonNull.INSTANCE;
        } else if (object instanceof List) {
            return actualArray((List<Object>) object, this);
        } else if (object instanceof Boolean) {
            return new ActualJsonBoolean((Boolean) object);
        }
        throw new IllegalArgumentException();
    }
}
