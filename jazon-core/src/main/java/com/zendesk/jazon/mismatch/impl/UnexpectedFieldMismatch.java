package com.zendesk.jazon.mismatch.impl;

import com.zendesk.jazon.mismatch.Mismatch;
import com.zendesk.jazon.mismatch.MismatchWithPathFactory;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class UnexpectedFieldMismatch implements Mismatch, MismatchWithPathFactory {
    private final String fieldName;

    public UnexpectedFieldMismatch(String fieldName) {
        this.fieldName = checkNotNull(fieldName);
    }

    @Override
    public String message() {
        return String.format("Unexpected field \"%s\" in object.", fieldName);
    }
}
