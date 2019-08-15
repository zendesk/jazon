package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;
import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

@ToString
@EqualsAndHashCode
public class TypeMismatch implements Mismatch, MismatchOccurrenceFactory {
    private static final Map<Class<? extends Actual>, String> JSON_TYPES = jsonTypes();

    private final Class<? extends Actual> expectedType;

    private final Class<? extends Actual> actualType;
    public TypeMismatch(Class<? extends Actual> expectedType, Class<? extends Actual> actualType) {
        this.expectedType = checkNotNull(expectedType);
        this.actualType = checkNotNull(actualType);
    }

    @Override
    public String message() {
        return String.format("Expected type: %s\nActual type:   %s", string(expectedType), string(actualType));
    }

    private String string(Class<? extends Actual> jsonType) {
        return ofNullable(JSON_TYPES.get(jsonType))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON type"));
    }

    private static Map<Class<? extends Actual>, String> jsonTypes() {
        HashMap<Class<? extends Actual>, String> jsonTypes = new HashMap<>();
        jsonTypes.put(ActualJsonObject.class, "Object");
        jsonTypes.put(ActualJsonArray.class, "Array");
        jsonTypes.put(ActualJsonString.class, "String");
        jsonTypes.put(ActualJsonNumber.class, "Number");
        jsonTypes.put(ActualJsonBoolean.class, "Boolean");
        jsonTypes.put(ActualJsonNull.class, "Null");
        return unmodifiableMap(jsonTypes);
    }
}
