package com.zendesk.jazon.expectation;

import com.zendesk.jazon.actual.ActualJsonBoolean;
import com.zendesk.jazon.actual.ActualJsonNumber;
import com.zendesk.jazon.actual.ActualJsonString;

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
    public static List<TranslatorWrapper> translators() {
        return asList(
                new TranslatorWrapper<>(Map.class, MapTranslator.wrapper()),
                new TranslatorWrapper<>(List.class, new ListTranslator()),
                new TranslatorWrapper<>(Set.class, new SetTranslator()),
                new TranslatorWrapper<>(Predicate.class, new PredicateTranslator()),
                new TranslatorWrapper<>(Number.class, new NumberTranslator()),
                new TranslatorWrapper<>(String.class, new StringTranslator()),
                new TranslatorWrapper<>(Boolean.class, new BooleanTranslator())
        );
    }

    private static class MapTranslator implements Translator<Map<CharSequence, Object>> {
        static Translator<Map> wrapper() {
            return new Translator<Map>() {
                private final Translator<Map<CharSequence, Object>> mapTranslatorInside = new MapTranslator();

                @Override
                public JsonExpectation jsonExpectation(Map object, TranslatorToExpectation translator) {
                    return mapTranslatorInside.jsonExpectation(object, translator);
                }
            };
        }

        @Override
        public JsonExpectation jsonExpectation(Map<CharSequence, Object> objectsMap, TranslatorToExpectation translator) {
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

    private static class ListTranslator implements Translator<List> {
        @Override
        public JsonExpectation jsonExpectation(List objectsList, TranslatorToExpectation translator) {
            Stream<JsonExpectation> stream = objectsList.stream()
                    .map(translator::expectation);
            List<JsonExpectation> expectations = stream.collect(toList());
            return new OrderedArrayExpectation(expectations);
        }
    }

    private static class SetTranslator implements Translator<Set> {
        @Override
        public JsonExpectation jsonExpectation(Set objectsSet, TranslatorToExpectation translator) {
            Stream<JsonExpectation> stream = objectsSet.stream()
                    .map(translator::expectation);
            Set<JsonExpectation> expectations = stream.collect(toSet());
            return new UnorderedArrayExpectation(expectations);
        }
    }

    private static class PredicateTranslator implements Translator<Predicate> {
        @Override
        public JsonExpectation jsonExpectation(Predicate predicate, TranslatorToExpectation translator) {
            return new PredicateExpectation(predicate);
        }
    }

    private static class NumberTranslator implements Translator<Number> {
        @Override
        public JsonExpectation jsonExpectation(Number number, TranslatorToExpectation translator) {
            return new PrimitiveValueExpectation<>(new ActualJsonNumber(number));
        }
    }

    private static class StringTranslator implements Translator<String> {
        @Override
        public JsonExpectation jsonExpectation(String string, TranslatorToExpectation translator) {
            return new PrimitiveValueExpectation<>(new ActualJsonString(string));
        }
    }

    private static class BooleanTranslator implements Translator<Boolean> {
        @Override
        public JsonExpectation jsonExpectation(Boolean bool, TranslatorToExpectation translator) {
            return new PrimitiveValueExpectation<>(new ActualJsonBoolean(bool));
        }
    }
}
