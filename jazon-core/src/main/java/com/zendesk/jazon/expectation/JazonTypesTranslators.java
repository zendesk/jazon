package com.zendesk.jazon.expectation;

import java.util.List;

import static java.util.Collections.singletonList;

public class JazonTypesTranslators {
    public static List<TranslatorWrapper> translators() {
        return singletonList(
                new TranslatorWrapper<>(AnyNumberOf.class, new AnyNumberOfTranslator())
        );
    }

    private static class AnyNumberOfTranslator implements Translator<AnyNumberOf> {
        @Override
        public JsonExpectation jsonExpectation(AnyNumberOf anyNumberOf, TranslatorToExpectation translator) {
            Object repeatedObject = anyNumberOf.getElementExpectation();
            JsonExpectation repeatedExpectation = translator.expectation(repeatedObject);
            return new ArrayEachElementExpectation(repeatedExpectation);
        }
    }
}
