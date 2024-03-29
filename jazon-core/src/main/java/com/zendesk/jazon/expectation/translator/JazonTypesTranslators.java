package com.zendesk.jazon.expectation.translator;

import com.zendesk.jazon.expectation.impl.AnyNumberOf;
import com.zendesk.jazon.expectation.impl.AnyNumberOfExpectation;
import com.zendesk.jazon.expectation.JsonExpectation;

import java.util.List;

import static java.util.Collections.singletonList;

public class JazonTypesTranslators {
    public static List<TranslatorMapping<?>> translators() {
        return singletonList(
                new TranslatorMapping<>(AnyNumberOf.class, new AnyNumberOfTranslator())
        );
    }

    private static class AnyNumberOfTranslator implements Translator<AnyNumberOf> {
        @Override
        public JsonExpectation jsonExpectation(AnyNumberOf anyNumberOf, TranslatorFacade translator) {
            Object repeatedObject = anyNumberOf.getElementExpectation();
            JsonExpectation repeatedExpectation = translator.expectation(repeatedObject);
            return new AnyNumberOfExpectation(repeatedExpectation);
        }
    }
}
