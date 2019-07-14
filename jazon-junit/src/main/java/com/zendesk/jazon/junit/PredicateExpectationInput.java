package com.zendesk.jazon.junit;

import java.util.function.Predicate;

public class PredicateExpectationInput implements JsonExpectationInput {
    private final Predicate<Object> predicate;

    public PredicateExpectationInput(Predicate<Object> predicate) {
        this.predicate = predicate;
    }

    public Predicate<Object> predicate() {
        return predicate;
    }
}
