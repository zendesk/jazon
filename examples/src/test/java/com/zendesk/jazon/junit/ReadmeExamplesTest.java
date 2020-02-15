package com.zendesk.jazon.junit;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static com.zendesk.jazon.junit.JazonJunitAdapter.assertThat;
import static java.util.Arrays.asList;

class ReadmeExamplesTest {
    private Predicate<Integer> ANY_ID = id -> id >= 0;
    private Predicate<String> ANY_ISO_DATETIME = datetime -> datetime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z");

    @Test
    void simpleAssertionPasses() {
        // when
        String response = "{\"firstname\": \"Steve\", \"lastname\": \"Jobs\"}";

        // then
        assertThat(response).matches(
                new JazonMap()
                        .with("firstname", "Steve")
                        .with("lastname", "Jobs")
        );
    }

    @Test
    void unorderedArrayAssertionPasses() {
        // when
        String response = "{" +
                "    \"id\": 95478,\n" +
                "    \"name\": \"Coca Cola\",\n" +
                "    \"tags\": [\"sprite\", \"pepsi\", \"7up\", \"fanta\", \"dr pepper\"]\n" +
                "}";

        // then
        assertThat(response).matches(
                new JazonMap()
                        .with("id", 95478)
                        .with("name", "Coca Cola")
                        .with("tags", set("pepsi", "dr pepper", "sprite", "fanta", "7up"))
        );
    }

    @Test
    void customAssertions() {
        // when
        String response = "{\n" +
                "    \"id\": 95478,\n" +
                "    \"name\": \"Coca Cola\",\n" +
                "    \"value\": \"133.30\",\n" +
                "    \"updated_at\": \"1990-06-19T12:19:10Z\"\n" +
                "}";

        // then
        assertThat(response).matches(
                new JazonMap()
                        .with("id", (Integer id) -> id >= 0)
                        .with("name", "Coca Cola")
                        .with("value", regex("\\d+\\.\\d\\d"))
                        .with("updated_at", Objects::nonNull)
        );
    }

    @Test
    void utilsExtraction() {
        // when
        String response = "{\n" +
                "    \"id\": 95478,\n" +
                "    \"name\": \"Coca Cola\",\n" +
                "    \"value\": \"133.30\",\n" +
                "    \"updated_at\": \"1990-06-19T12:19:10Z\"\n" +
                "}";

        // then
        assertThat(response).matches(
                new JazonMap()
                        .with("id", ANY_ID)
                        .with("name", "Coca Cola")
                        .with("value", "133.30")
                        .with("updated_at", ANY_ISO_DATETIME)
        );
    }

    @Test
    void utilsExtractionToDomainObjects() {
        // when
        String response = "{\n" +
                "    \"id\": 95478,\n" +
                "    \"name\": \"Coca Cola\",\n" +
                "    \"value\": \"10.00\",\n" +
                "    \"updated_at\": \"1990-06-19T12:19:10Z\"\n" +
                "}";

        // then
        assertThat(response).matches(deal("Coca Cola", "10.00"));
        assertThat(response).matches(
                asList(
                        deal("Coca Cola", "10.00"),
                        deal("Pepsi", "9.00"),
                        deal("Fanta", "10.00"),
                        deal("Sprite", "10.00"),
                        deal("Dr Pepper", "12.00")
                )
        );
    }

    private JazonMap deal(String name, String value) {
        return new JazonMap()
                .with("id", ANY_ID)
                .with("name", name)
                .with("value", value)
                .with("updated_at", ANY_ISO_DATETIME);
    }

    private Predicate<String> regex(String regex) {
        return val -> val.matches(regex);
    }

    private Set<Object> set(Object... elements) {
        HashSet<Object> result = new HashSet<>(elements.length);
        result.addAll(asList(elements));
        return result;
    }
}
