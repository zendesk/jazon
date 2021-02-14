package com.zendesk.jazon.expectation;

import com.zendesk.jazon.actual.ActualJsonString;
import groovy.lang.Closure;
import groovy.lang.GString;

import java.util.Optional;

import static java.util.Optional.of;

public class SpockExpectationFactory implements ExpectationFactory {

    @Override
    public Optional<JsonExpectation> expectationKek(Object object) {
        if (object instanceof GString) {
            GString interpolatedString = (GString) object;
            return of(new PrimitiveValueExpectation<>(new ActualJsonString(interpolatedString.toString())));
        } else if (object instanceof Closure) {
            Closure<Boolean> closure = (Closure<Boolean>) object;
            return of(new PredicateExpectation(closure::call));
        }
        return Optional.empty();
    }
}
