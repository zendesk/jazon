package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.actual.Actual;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class ArrayUnexpectedElementsMismatch implements Mismatch, MismatchOccurrenceFactory {
    private final List<Actual> unexpectedElements;

    public ArrayUnexpectedElementsMismatch(List<Actual> unexpectedElements) {
        this.unexpectedElements = checkNotNull(unexpectedElements);
    }

    @Override
    public String message() {
        return String.format("Array contains unexpected items: %s", unexpectedElements);
    }
}
