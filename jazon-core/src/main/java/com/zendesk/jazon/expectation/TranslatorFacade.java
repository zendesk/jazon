package com.zendesk.jazon.expectation;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TranslatorFacade {
    private final List<TranslatorMapping<?>> translatorMappings;

    public JsonExpectation expectation(Object object) {
        // give me the expectation-factory for this object
        // use the expectation factory to craft the expecattion giving it: the object, this translator
        // NOPE

        if (object == null) {
            return new NullExpectation();
        }
        for (TranslatorMapping<?> translatorMapping : translatorMappings) {
            if (translatorMapping.supports(object)) {
                return translatorMapping.jsonExpectation(object, this);
            }
        }
        throw new IllegalArgumentException(String.format("Could not map this object to expectation: %s", object));
    }
}
