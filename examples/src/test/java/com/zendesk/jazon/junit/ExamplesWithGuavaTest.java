package com.zendesk.jazon.junit;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.zendesk.jazon.junit.JazonJunitAdapter.assertThat;
import static java.util.Arrays.asList;

class ExamplesWithGuavaTest {

    @Test
    void simpleTest() {
        // given
        String actualJson = "{\"value\": 123}";
        Map<String, Object> expectedJsonAsMap = ImmutableMap.<String, Object>builder()
                .put("value", 50)
                .build();

        // then
        assertThat(actualJson).matches(expectedJsonAsMap);
    }

    @Test
    void testWithNestedArray() {
        // given
        String actualJson = "{" +
                "\"value\": 50," +
                "\"tags\": [\"blue\", \"black\", \"red\"]" +
                "}";

        // then
        assertThat(actualJson).matches(
                deal(50, asList("blue", "pink", "red"))
        );
    }

    @Test
    void testWithRootArray() {
        // given
        String actualJson = "[\"blue\", \"black\", \"red\"]";

        // then
        assertThat(actualJson).matches(asList("blue", "pink", "red"));
    }

    @Test
    void testRegex() {
        // given
        String actualJson = "[\"blue\", \"black\", \"red\"]";

        // then
        assertThat(actualJson).matches(
                asList(
                        regex("bl.*"),
                        regex("bl.*"),
                        regex("r.*")
                )
        );
    }

    private Predicate<String> regex(String reg) {
        return s -> s.matches(reg);
    }

    private static Map<String, Object> deal(int value, List<String> tags) {
        return ImmutableMap.<String, Object>builder()
                .put("value", value)
                .put("tags", tags)
                .build();
    }
}
