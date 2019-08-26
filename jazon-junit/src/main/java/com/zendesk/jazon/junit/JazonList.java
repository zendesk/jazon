package com.zendesk.jazon.junit;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

/**
 * This class exists to allow to pass a lambda-predicate to the same interface as other typical objects like String,
 * Integer, List, etc. are passed. This is due to the limitation that {@code Object} is not effectively a supertype of
 * lambda expression.
 */
@EqualsAndHashCode
public class JazonList {
    private final List<JsonExpectationInput> list = new ArrayList<>();

    public JazonList(Predicate<?>... predicates) {
        checkNotNull(predicates);
        for (Predicate<?> element : predicates) {
            list.add(new PredicateExpectationInput<>(element));
        }
    }

    public JazonList(Object... objects) {
        checkNotNull(objects);
        for (Object element : objects) {
            list.add(new ObjectExpectationInput(element));
        }
    }

    public JazonList with(Object object) {
        list.add(new ObjectExpectationInput(object));
        return this;
    }

    public <T> JazonList with(Predicate<T> predicate) {
        list.add(new PredicateExpectationInput<>(predicate));
        return this;
    }

    public List<JsonExpectationInput> list() {
        return list;
    }
}
