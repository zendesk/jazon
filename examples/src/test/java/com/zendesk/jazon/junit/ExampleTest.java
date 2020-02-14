package com.zendesk.jazon.junit;

import org.junit.jupiter.api.Test;

import static com.zendesk.jazon.junit.JazonJunitAdapter.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExampleTest {

    @Test
    void testRegex() {
        // given
        String actualJson = "{" +
                "   \"first\": \"blue\"," +
                "   \"second\": \"black\"," +
                "   \"third\": \"red\"" +
                "}";

        // then
        assertThat(actualJson).matches(
                new JazonMap()
                        .with("first", (String s) -> s.matches("bl.*"))
                        .with("second", s -> ((String)s).matches("bl.*"))
                        .with("third", "red")
        );
    }

    @Test
    void testRegexTypeMismatch() {
        // given
        String actualJson = "{" +
                "   \"first\": 55" +
                "}";

        // then
        assertThat(actualJson).matches(
                new JazonMap()
                        .with("first", (String s) -> s.matches("bl.*"))
        );
    }

    @Test
    void testPredicatedWithDeeplyNestedException() {
        assertThrows(AssertionError.class, () -> {
            // given
            String actualJson = "{" +
                    "   \"first\": 55" +
                    "}";

            // then
            assertThat(actualJson).matches(
                    new JazonMap()
                            .with("first", this::complexOperation)
            );
        });
    }

    private boolean complexOperation(Integer number) {
        return failingOperation(number + 10);
    }

    private boolean failingOperation(int number) {
        throw new RuntimeException("an intentional exception");
    }
}
