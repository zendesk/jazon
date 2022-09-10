package com.zendesk.jazon.expectation;

import com.zendesk.jazon.actual.ActualJsonString;
import groovy.lang.Closure;
import groovy.lang.GString;

import java.util.List;

import static java.util.Arrays.asList;

public class SpockTranslators {
    public static List<TranslatorMapping> translators() {
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

    private static class ClosureTranslator extends KekWrapper<Closure, Closure<Boolean>> {
        @Override
        JsonExpectation jsonExpectationKek(Closure<Boolean> closure, TranslatorFacade translator) {
            return new PredicateExpectation(closure::call);
        }
    }

    private static abstract class KekWrapper<T, T_GENERIC extends T> implements Translator<T> {
        abstract JsonExpectation jsonExpectationKek(T_GENERIC object, TranslatorFacade translator);

        @Override
        public JsonExpectation jsonExpectation(T object, TranslatorFacade translator) {
            return jsonExpectationKek((T_GENERIC) object, translator);
        }
    }
}
