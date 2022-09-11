package com.zendesk.jazon.expectation.translator;

import com.zendesk.jazon.actual.ActualJsonString;
import com.zendesk.jazon.expectation.JsonExpectation;
import com.zendesk.jazon.expectation.impl.PredicateExpectation;
import com.zendesk.jazon.expectation.impl.PrimitiveValueExpectation;
import groovy.lang.Closure;
import groovy.lang.GString;

import java.util.List;

import static java.util.Arrays.asList;

public class SpockTranslators {
    public static List<TranslatorMapping<?>> translators() {
        return asList(
                new TranslatorMapping<>(GString.class, new GStringTranslator()),
                new TranslatorMapping<>(Closure.class, new ClosureTranslator())
        );
    }

    private static class GStringTranslator implements Translator<GString> {
        @Override
        public JsonExpectation jsonExpectation(GString gstring, TranslatorFacade translator) {
            return new PrimitiveValueExpectation<>(new ActualJsonString(gstring.toString()));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static class ClosureTranslator implements Translator<Closure> {
        @Override
        public JsonExpectation jsonExpectation(Closure object, TranslatorFacade translator) {
            Closure<Boolean> booleanClosure = (Closure<Boolean>) object;
            return new PredicateExpectation(booleanClosure::call);
        }
    }
}
