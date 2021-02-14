package com.zendesk.jazon.junit;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.MatcherFactory;
import com.zendesk.jazon.actual.GsonActualFactory;
import com.zendesk.jazon.expectation.DefaultTranslators;
import com.zendesk.jazon.expectation.JazonTypesTranslators;
import com.zendesk.jazon.expectation.JunitTranslators;
import com.zendesk.jazon.expectation.TranslatorToExpectation;
import com.zendesk.jazon.expectation.TranslatorWrapper;

import java.util.LinkedList;
import java.util.List;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

public class JazonJunitAdapter {
    private static final MatcherFactory matcherFactory = new MatcherFactory(
            new TranslatorToExpectation(translators()),
            new GsonActualFactory()
    );

    private final String actualJson;

    public JazonJunitAdapter(String actualJson) {
        this.actualJson = checkNotNull(actualJson);
    }

    public static JazonJunitAdapter assertThat(String actualJson) {
        return new JazonJunitAdapter(actualJson);
    }

    public void matches(JazonMap jazonMap) {
        matchExpectedObject(jazonMap.map());
    }

    public void matches(Object expected) {
        matchExpectedObject(expected);
    }

    private void matchExpectedObject(Object expected) {
        MatchResult matchResult = matcherFactory.matcher()
                .expected(expected)
                .actual(actualJson)
                .match();
        if (matchResult.ok()) {
            return;
        }
        String mismatchMessageTemplate = "\n-----------------------------------\nJSON MISMATCH:\n%s\n-----------------------------------\n";
        throw new AssertionError(String.format(mismatchMessageTemplate, matchResult.message()));
    }

    private static List<TranslatorWrapper> translators() {
        List<TranslatorWrapper> result = new LinkedList<>();
        result.addAll(DefaultTranslators.translators());
        result.addAll(JazonTypesTranslators.translators());
        result.addAll(JunitTranslators.translators());
        return result;
    }
}
