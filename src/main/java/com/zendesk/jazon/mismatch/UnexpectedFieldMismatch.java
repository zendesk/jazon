package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class UnexpectedFieldMismatch<T> implements Mismatch, MismatchWithPathFactory {
    private final Class<? extends Actual> actualType;

    public UnexpectedFieldMismatch(Class<? extends Actual> actualType) {
        this.actualType = checkNotNull(actualType);
    }
}
