package com.zendesk.jazon.junit;

import java.util.function.Predicate;

public class PredicateExpectationInput<T> implements JsonExpectationInput {
    private final Predicate<T> predicate;

    public PredicateExpectationInput(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public Predicate<?> predicate() {
        return predicate;
    }
}
