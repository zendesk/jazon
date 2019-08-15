package com.zendesk.jazon.junit;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.MatcherFactory;
import com.zendesk.jazon.actual.GsonActualFactory;
import com.zendesk.jazon.actual.ObjectsActualFactory;
import com.zendesk.jazon.expectation.JunitExpectationFactory;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

public class JazonJunitAdapter {
    private static final GsonActualFactory GSON_ACTUAL_FACTORY = new GsonActualFactory();
    private static final MatcherFactory matcherFactory = new MatcherFactory(
            new JunitExpectationFactory(),
            new ObjectsActualFactory()
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
                .actual(GSON_ACTUAL_FACTORY.actual(actualJson))
                .match();
        if (matchResult.ok()) {
            return;
        }
        String mismatchMessageTemplate = "\n-----------------------------------\nJSON MISMATCH:\n%s\n-----------------------------------\n";
        throw new AssertionError(String.format(mismatchMessageTemplate, matchResult.message()), new RuntimeException());
    }
}
