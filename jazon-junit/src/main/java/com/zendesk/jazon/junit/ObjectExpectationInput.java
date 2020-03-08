package com.zendesk.jazon.junit;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ObjectExpectationInput implements JsonExpectationInput {
    private final Object object;

    ObjectExpectationInput(Object object) {
        this.object = object;
    }

    public Object object() {
        return object;
    }
}
