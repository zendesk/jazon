package com.zendesk.jazon.expectation;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class CompoundExpectationFactory implements ExpectationFactory {
    private final Iterable<ExpectationFactory> factories;

    @Override
    public Optional<JsonExpectation> expectationKek(Object object) {
        for (ExpectationFactory factory : factories) {
            Optional<JsonExpectation> expectation = factory.expectationKek(object);
            if (expectation.isPresent()) {
                return expectation;
            }
        }
        return Optional.empty();
    }
}
