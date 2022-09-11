package com.zendesk.jazon.expectation.translator;

import com.zendesk.jazon.actual.ActualJsonBoolean;
import com.zendesk.jazon.actual.ActualJsonNumber;
import com.zendesk.jazon.actual.ActualJsonString;
import com.zendesk.jazon.expectation.JsonExpectation;
import com.zendesk.jazon.expectation.impl.ObjectExpectation;
import com.zendesk.jazon.expectation.impl.OrderedArrayExpectation;
import com.zendesk.jazon.expectation.impl.PredicateExpectation;
import com.zendesk.jazon.expectation.impl.PrimitiveValueExpectation;
import com.zendesk.jazon.expectation.impl.UnorderedArrayExpectation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class DefaultTranslators {
    public static List<TranslatorMapping<?>> translators() {
        return asList(
                new TranslatorMapping<>(Map.class, new MapTranslator()),
                new TranslatorMapping<>(List.class, new ListTranslator()),
                new TranslatorMapping<>(Set.class, new SetTranslator()),
                new TranslatorMapping<>(Predicate.class, new PredicateTranslator()),
                new TranslatorMapping<>(Number.class, new NumberTranslator()),
                new TranslatorMapping<>(String.class, new StringTranslator()),
                new TranslatorMapping<>(Boolean.class, new BooleanTranslator())
        );
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static class MapTranslator implements Translator<Map> {
        @Override
        public JsonExpectation jsonExpectation(Map object, TranslatorFacade translator) {
            Map<CharSequence, Object> objectsMap = (Map<CharSequence, Object>) object;
            LinkedHashMap<String, JsonExpectation> expectationsMap = objectsMap.entrySet()
                    .stream()
                    .collect(
                            toMap(
                                    e -> e.getKey().toString(),
                                    e -> translator.expectation(e.getValue()),
                                    (a, b) -> a,
                                    () -> new LinkedHashMap<>(objectsMap.size())
                            )
                    );
            return new ObjectExpectation(expectationsMap);

        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static class ListTranslator implements Translator<List> {
        @Override
        public JsonExpectation jsonExpectation(List objectsList, TranslatorFacade translator) {
            Stream<JsonExpectation> stream = objectsList.stream()
                    .map(translator::expectation);
            List<JsonExpectation> expectations = stream.collect(toList());
            return new OrderedArrayExpectation(expectations);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static class SetTranslator implements Translator<Set> {
        @Override
        public JsonExpectation jsonExpectation(Set objectsSet, TranslatorFacade translator) {
            Stream<JsonExpectation> stream = objectsSet.stream()
                    .map(translator::expectation);
            Set<JsonExpectation> expectations = stream.collect(toSet());
            return new UnorderedArrayExpectation(expectations);
        }
    }

    @SuppressWarnings("rawtypes")
    private static class PredicateTranslator implements Translator<Predicate> {
        @Override
        public JsonExpectation jsonExpectation(Predicate predicate, TranslatorFacade translator) {
            return new PredicateExpectation(predicate);
        }
    }

    private static class NumberTranslator implements Translator<Number> {
        @Override
        public JsonExpectation jsonExpectation(Number number, TranslatorFacade translator) {
            return new PrimitiveValueExpectation<>(new ActualJsonNumber(number));
        }
    }

    private static class StringTranslator implements Translator<String> {
        @Override
        public JsonExpectation jsonExpectation(String string, TranslatorFacade translator) {
            return new PrimitiveValueExpectation<>(new ActualJsonString(string));
        }
    }

    private static class BooleanTranslator implements Translator<Boolean> {
        @Override
        public JsonExpectation jsonExpectation(Boolean bool, TranslatorFacade translator) {
            return new PrimitiveValueExpectation<>(new ActualJsonBoolean(bool));
        }
    }
}
