package com.zendesk.jazon.mismatch.impl;

import com.zendesk.jazon.mismatch.Mismatch;
import com.zendesk.jazon.mismatch.MismatchWithPathFactory;
import lombok.ToString;

@ToString
public enum PredicateMismatch implements Mismatch, MismatchWithPathFactory {
    INSTANCE;

    @Override
    public String message() {
        return "Custom predicate does not match the value.";
    }
}
