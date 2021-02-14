package com.zendesk.jazon.junit;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.MatcherFactory;
import com.zendesk.jazon.mismatch.PredicateExecutionFailedMismatch;
import com.zendesk.jazon.mismatch.PrimitiveValueMismatch;
import org.junit.jupiter.api.Test;

import static com.zendesk.jazon.expectation.Expectations.anyNumberOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class JunitSpecificMatcherTest {

//    private static final GsonActualFactory GSON_ACTUAL_FACTORY = new GsonActualFactory();
    private static final MatcherFactory matcherFactory = null;
//    private static final MatcherFactory matcherFactory = new MatcherFactory(
//            new JunitExpectationFactory(),
//            new ObjectsActualFactory()
//    );

    @Test
    void testRegex() {
        // given
        JazonMap expected = new JazonMap()
                .with("first", (String s) -> s.matches("bl.*"))
                .with("second", s -> ((String) s).matches("bl.*"))
                .with("third", "red");
        String actualJson = "{" +
                "   \"first\": \"blue\"," +
                "   \"second\": \"black\"," +
                "   \"third\": \"red\"" +
                "}";

        // when
        MatchResult matchResult = match(expected, actualJson);

        // then
        assertTrue(matchResult.ok());
    }

    @Test
    void testRegexTypeMismatch() {
        // given
        JazonMap expected = new JazonMap()
                .with("first", (String s) -> s.matches("bl.*"));
        String actualJson = "{" +
                "   \"first\": 55" +
                "}";

        // when
        MatchResult matchResult = match(expected, actualJson);

        // then
        assertFalse(matchResult.ok());
        assertEquals(matchResult.mismatch().expectationMismatch().getClass(), PredicateExecutionFailedMismatch.class);
        assertTrue(
                matchResult.mismatch().expectationMismatch().message().startsWith(
                        "Exception occurred on predicate evaluation: \n\njava.lang.ClassCastException"
                )
        );
        assertEquals(matchResult.mismatch().path(), "$.first");
    }

    @Test
    void testPredicatedWithDeeplyNestedException() {
        // given
        JazonMap expected = new JazonMap()
                .with("first", this::complexOperation);
        String actualJson = "{" +
                "   \"first\": 55" +
                "}";

        // when
        MatchResult matchResult = match(expected, actualJson);

        // then
        assertFalse(matchResult.ok());
        assertEquals(matchResult.mismatch().expectationMismatch().getClass(), PredicateExecutionFailedMismatch.class);
        assertTrue(
                matchResult.mismatch().expectationMismatch().message().startsWith(
                        "Exception occurred on predicate evaluation: \n\n" +
                                "java.lang.RuntimeException: an intentional exception for value 55"
                )
        );
        assertEquals(matchResult.mismatch().path(), "$.first");
    }

    @Test
    void testAnyNumberOfExpectationSuccess() {
        // given
        JazonMap expected = new JazonMap()
                .with("animals", anyNumberOf("cat"));
        String actualJson = "{" +
                "   \"animals\": [\"cat\", \"cat\", \"cat\", \"cat\", \"cat\", \"cat\", \"cat\"]" +
                "}";

        // when
        MatchResult matchResult = match(expected, actualJson);

        // then
        assertTrue(matchResult.ok());
    }

    @Test
    void testAnyNumberOfExpectationFailure() {
        // given
        JazonMap expected = new JazonMap()
                .with("animals", anyNumberOf("cat"));
        String actualJson = "{" +
                "   \"animals\": [\"cat\", \"cat\", \"dog\", \"cat\", \"cat\", \"cat\"]" +
                "}";

        // when
        MatchResult matchResult = match(expected, actualJson);

        // then
        assertFalse(matchResult.ok());
        assertEquals(matchResult.mismatch().expectationMismatch().getClass(), PrimitiveValueMismatch.class);
        assertEquals(matchResult.mismatch().path(), "$.animals.2");
    }

    private boolean complexOperation(Integer number) {
        return failingOperation(number);
    }

    private boolean failingOperation(int number) {
        throw new RuntimeException("an intentional exception for value " + number);
    }

    private MatchResult match(JazonMap expected, String actualJson) {
        return matcherFactory.matcher()
                .expected(expected.map())
                .actual(actualJson)
                .match();
    }
}
