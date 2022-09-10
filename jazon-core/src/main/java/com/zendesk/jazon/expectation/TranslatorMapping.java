package com.zendesk.jazon.expectation;

import lombok.AllArgsConstructor;

//FIXME this should not be public class. nope i do not think so as long as user-defined mappings can exist outside of jazon package (should they?)
@AllArgsConstructor
public class TranslatorMapping<T> {
    private final Class<T> klass;
    private final Translator<T> translator;

    boolean supports(Object object) {
        return klass.isInstance(object);
    }

    JsonExpectation jsonExpectation(Object object, TranslatorFacade translator) {
        return this.translator.jsonExpectation(cast(object), translator);
    }

    private T cast(Object object) {
        if (!supports(object)) {
            throw new IllegalArgumentException("Given object is not supported: " + object);
        }
        return klass.cast(object);
    }
}
