package com.zendesk.jazon.mismatch;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class ArrayElementMismatch implements JsonMismatch, LocJsonMismatchFactory {
    private final int elementIndex;
    private final JsonMismatch elementMismatch;

    public ArrayElementMismatch(int elementIndex, JsonMismatch elementMismatch) {
        this.elementIndex = elementIndex;
        this.elementMismatch = checkNotNull(elementMismatch);
    }
}
