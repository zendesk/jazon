package com.zendesk.jazon.mismatch;

import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class ArrayLackingElementsMismatch implements Mismatch, MismatchWithPathFactory {
    private final Collection<JsonExpectation> lackingElements;

    public ArrayLackingElementsMismatch(Collection<JsonExpectation> lackingElements) {
        this.lackingElements = checkNotNull(lackingElements);
    }

    @Override
    public String message() {
        return String.format("Array lacks the items: %s", lackingElements);
    }
}
