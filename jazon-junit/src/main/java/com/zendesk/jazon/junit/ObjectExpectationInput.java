package com.zendesk.jazon.junit;

public class ObjectExpectationInput implements JsonExpectationInput {
    private final Object object;

    public ObjectExpectationInput(Object object) {
        this.object = object;
    }

    public Object object() {
        return object;
    }
}
