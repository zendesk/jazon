package com.zendesk.jazon.mismatch;

import com.google.common.collect.ImmutableMap;
import com.zendesk.jazon.actual.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;

@ToString
@EqualsAndHashCode
public class TypeMismatch implements Mismatch, MismatchWithPathFactory {
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
        return ImmutableMap.<Class<? extends Actual>, String>builder()
                .put(ActualJsonObject.class, "Object")
                .put(ActualJsonArray.class, "Array")
                .put(ActualJsonString.class, "String")
                .put(ActualJsonNumber.class, "Number")
                .put(ActualJsonBoolean.class, "Boolean")
                .put(ActualJsonNull.class, "Null")
                .build();
    }
}
