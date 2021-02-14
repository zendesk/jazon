package com.zendesk.jazon.expectation;

import com.zendesk.jazon.actual.ActualJsonBoolean;
import com.zendesk.jazon.actual.ActualJsonNumber;
import com.zendesk.jazon.actual.ActualJsonString;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Optional.of;

public class DefaultExpectationFactory implements ExpectationFactory {

    @Override
    public Optional<JsonExpectation> expectationKek(Object object) {
        if (object instanceof Map) {
            // here we need the whole main ExpectationFactory (translator)
            return of(ExpectationFactory.objectExpectation((Map<CharSequence, Object>) object, this));
        } else if (object instanceof Number) {
            return of(new PrimitiveValueExpectation<>(new ActualJsonNumber((Number) object)));
        } else if (object instanceof String) {
            return of(new PrimitiveValueExpectation<>(new ActualJsonString((String) object)));
        } else if (object instanceof Boolean) {
            return of(new PrimitiveValueExpectation<>(new ActualJsonBoolean((Boolean) object)));
        } else if (object instanceof List) {
            return of(ExpectationFactory.expectedOrderedArray((List<Object>) object, this));
        } else if (object instanceof Set) {
            return of(ExpectationFactory.expectedUnorderedArray((Set<Object>) object, this));
        } else if (object == null) {
            return of(new NullExpectation());
        } else if (object instanceof Predicate) {
            return of(new PredicateExpectation((Predicate<Object>) object));
        }
        return Optional.empty();
    }
}
