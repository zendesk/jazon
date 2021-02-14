package com.zendesk.jazon.expectation;

import com.zendesk.jazon.actual.ActualJsonString;
import groovy.lang.Closure;
import groovy.lang.GString;

import java.util.List;

import static java.util.Arrays.asList;

public class SpockTranslators {
    public static List<TranslatorWrapper> translators() {
        return asList(
                new TranslatorWrapper<>(GString.class, new GStringTranslator()),
                new TranslatorWrapper<>(Closure.class, new ClosureTranslator())
        );
    }

    private static class GStringTranslator implements Translator<GString> {
        @Override
        public JsonExpectation jsonExpectation(GString gstring, TranslatorToExpectation translator) {
            return new PrimitiveValueExpectation<>(new ActualJsonString(gstring.toString()));
        }
    }

    private static class ClosureTranslator extends KekWrapper<Closure, Closure<Boolean>> {
        @Override
        JsonExpectation jsonExpectationKek(Closure<Boolean> closure, TranslatorToExpectation translator) {
            return new PredicateExpectation(closure::call);
        }
    }

    private static abstract class KekWrapper<T, T_GENERIC extends T> implements Translator<T> {
        abstract JsonExpectation jsonExpectationKek(T_GENERIC object, TranslatorToExpectation translator);

        @Override
        public JsonExpectation jsonExpectation(T object, TranslatorToExpectation translator) {
            return jsonExpectationKek((T_GENERIC) object, translator);
        }
    }
}
