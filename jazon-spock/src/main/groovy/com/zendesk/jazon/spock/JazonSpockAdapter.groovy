package com.zendesk.jazon.spock

import com.zendesk.jazon.MatchResult
import com.zendesk.jazon.MatcherFactory
import com.zendesk.jazon.actual.GsonActualFactory
import com.zendesk.jazon.expectation.DefaultTranslators
import com.zendesk.jazon.expectation.JazonTypesTranslators
import com.zendesk.jazon.expectation.SpockTranslators
import com.zendesk.jazon.expectation.TranslatorToExpectation
import com.zendesk.jazon.expectation.TranslatorWrapper

class JazonSpockAdapter {
    private static final MatcherFactory MATCHER_FACTORY = new MatcherFactory(
            new TranslatorToExpectation(translators()),
            new GsonActualFactory()
    )
    private final String json

    private JazonSpockAdapter(String json) {
        this.json = json
    }

    static JazonSpockAdapter jazon(String json) {
        return new JazonSpockAdapter(json)
    }

    boolean matches(Map jsonAsMap) {
        return match(jsonAsMap)
    }

    boolean matches(List jsonAsList) {
        return match(jsonAsList)
    }

    private boolean match(Object expected) {
        MatchResult matchResult = MATCHER_FACTORY.matcher()
                .expected(expected)
                .actual(json)
                .match()
        if (matchResult.ok()) {
            return true
        }
        throw new AssertionError(errorMessage(matchResult))
    }

    private static GString errorMessage(MatchResult matchResult) {
        "\n-----------------------------------\nJSON MISMATCH:\n${matchResult.message()}\n-----------------------------------\n"
    }

    private static List<TranslatorWrapper> translators() {
        DefaultTranslators.translators() + JazonTypesTranslators.translators() + SpockTranslators.translators()
    }
}
