package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class TypeMismatch implements JsonMismatch {
    private final Class<? extends Actual> expectedType;
    private final Class<? extends Actual> actualType;

    public TypeMismatch(Class<? extends Actual> expectedType, Class<? extends Actual> actualType) {
        this.expectedType = checkNotNull(expectedType);
        this.actualType = checkNotNull(actualType);
    }

    @Override
    public String message() {
        return "types do not match";
    }
}
