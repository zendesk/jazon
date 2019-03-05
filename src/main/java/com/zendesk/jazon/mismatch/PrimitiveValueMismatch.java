package com.zendesk.jazon.mismatch;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class PrimitiveValueMismatch<T> implements JsonMismatch {
    private final T expected;
    private final T actual;

    public PrimitiveValueMismatch(T expected, T actual) {
        this.expected = checkNotNull(expected);
        this.actual = checkNotNull(actual);
    }

    @Override
    public String message() {
        return "ale beka primitive mismacz";   //FIXME
    }
}
