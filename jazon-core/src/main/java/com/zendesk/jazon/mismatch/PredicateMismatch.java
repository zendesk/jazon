package com.zendesk.jazon.mismatch;

import lombok.ToString;

@ToString
public enum PredicateMismatch implements Mismatch, MismatchOccurrenceFactory {
    INSTANCE;

    @Override
    public String message() {
        return "Custom predicate does not match the value.";
    }
}
