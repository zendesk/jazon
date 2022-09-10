package com.zendesk.jazon.expectation;

import java.util.Optional;

import static java.util.Optional.of;

class JazonTypesExpectationFactory implements ExpectationFactory {

    @Override
    public Optional<JsonExpectation> expectationMaybe(Object object) {
        if (object instanceof AnyNumberOf) {
            Object repeatedObject = ((AnyNumberOf) object).getElementExpectation();
            JsonExpectation repeatedExpectation = expectation(repeatedObject);
            return of(new ArrayEachElementExpectation(repeatedExpectation));
        }
        return Optional.empty();
    }
}
