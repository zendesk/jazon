package com.zendesk.jazon.expectation.translator;

import com.zendesk.jazon.expectation.JsonExpectation;
import com.zendesk.jazon.expectation.ObjectExpectation;
import com.zendesk.jazon.expectation.PredicateExpectation;
import com.zendesk.jazon.junit.JazonMap;
import com.zendesk.jazon.junit.JsonExpectationInput;
import com.zendesk.jazon.junit.ObjectExpectationInput;
import com.zendesk.jazon.junit.PredicateExpectationInput;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

public class JunitTranslators {
    public static List<TranslatorMapping<?>> translators() {
        return asList(
                new TranslatorMapping<>(ObjectExpectationInput.class, new ObjectExpectationInputTranslator()),
                new TranslatorMapping<>(PredicateExpectationInput.class, new PredicateExpectationInputTranslator()),
                new TranslatorMapping<>(JazonMap.class, new JazonMapTranslator())
        );
    }

    private static class ObjectExpectationInputTranslator implements Translator<ObjectExpectationInput> {
        @Override
        public JsonExpectation jsonExpectation(ObjectExpectationInput objectExpectationInput, TranslatorFacade translator) {
            return translator.expectation(objectExpectationInput.object());
        }
    }

    private static class PredicateExpectationInputTranslator implements Translator<PredicateExpectationInput> {
        @Override
        public JsonExpectation jsonExpectation(PredicateExpectationInput predicateExpectationInput, TranslatorFacade translator) {
            return new PredicateExpectation(predicateExpectationInput.predicate());
        }
    }

    private static class JazonMapTranslator implements Translator<JazonMap> {
        @Override
        public JsonExpectation jsonExpectation(JazonMap jazonMap, TranslatorFacade translator) {
            HashMap<CharSequence, Object> map = copied(jazonMap.map());
            //FIXME code duplication
            LinkedHashMap<String, JsonExpectation> expectationsMap = map.entrySet()
                    .stream()
                    .collect(
                            toMap(
                                    e -> e.getKey().toString(),
                                    e -> translator.expectation(e.getValue()),
                                    (a, b) -> a,
                                    () -> new LinkedHashMap<>(map.size())
                            )
                    );
            return new ObjectExpectation(expectationsMap);
        }

        /**
         * Returns Map<CharSequence, Object> converted from Map<String, JsonExpectationInput>
         */
        private HashMap<CharSequence, Object> copied(Map<String, JsonExpectationInput> map) {
            return map
                    .entrySet()
                    .stream()
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));
        }
    }
}
