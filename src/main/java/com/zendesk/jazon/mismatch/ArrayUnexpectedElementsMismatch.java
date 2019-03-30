package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class ArrayUnexpectedElementsMismatch implements JsonMismatch, LocJsonMismatchFactory {
    private final List<Actual> unexpectedElements;

    public ArrayUnexpectedElementsMismatch(List<Actual> unexpectedElements) {
        this.unexpectedElements = checkNotNull(unexpectedElements);
    }
}
