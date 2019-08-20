package com.zendesk.jazon.junit;

import lombok.EqualsAndHashCode;

import java.util.function.Predicate;

@EqualsAndHashCode
public class PredicateExpectationInput<T> implements JsonExpectationInput {
    private final Predicate<T> predicate;

    PredicateExpectationInput(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public Predicate<?> predicate() {
        return predicate;
    }
}
