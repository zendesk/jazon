package com.zendesk.jazon.mismatch;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class ObjectFieldMismatch implements JsonMismatch {
    private final String fieldName;
    private final JsonMismatch elementMismatch;

    public ObjectFieldMismatch(String fieldName, JsonMismatch elementMismatch) {
        this.fieldName = checkNotNull(fieldName);
        this.elementMismatch = checkNotNull(elementMismatch);
    }
}
