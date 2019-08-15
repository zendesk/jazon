package com.zendesk.jazon.junit;

import org.junit.Test;

import static com.zendesk.jazon.junit.JazonJunitAdapter.assertThat;


public class NicerJunitSpikeExampleTest {

    @Test
    public void testRegex() {
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
    public void testRegexTypeMismatch() {
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
}
