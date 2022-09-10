package com.zendesk.jazon.expectation;

import java.util.Optional;

import static java.util.Arrays.asList;

/**
 * This class exists to handle assertions in tests when instance of Expectation is needed in the assertion-expectation
 * e.g. equality-comparing of `Mismatch` instances that store Expectation instance inside itself.
 * Without this class we would have to write lot of verbose constructor-calls like `new ObjectExpectation(...)` etc.
 */
public class TestExpectationFactory implements ExpectationFactory {
    private final Iterable<ExpectationFactory> factories;

    public TestExpectationFactory() {
        this.factories = asList(
                new DefaultExpectationFactory(),
                new JazonTypesExpectationFactory()
        );
    }

    @Override
    public Optional<JsonExpectation> expectationMaybe(Object object) {
        for (ExpectationFactory factory : factories) {
            Optional<JsonExpectation> expectation = factory.expectationMaybe(object);
            if (expectation.isPresent()) {
                return expectation;
            }
        }
        return Optional.empty();
    }
}
