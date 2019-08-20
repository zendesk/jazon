package com.zendesk.jazon.junit;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JazonMapTest {

    @Test
    void simpleMap() {
        // given
        JazonMap jazonMap = new JazonMap()
                .with("name", "Leo Messi")
                .with("email", "leo@messi.com")
                .with("height", 170)
                .with("is_cool", true);

        // when
        Map<String, JsonExpectationInput> map = jazonMap.map();

        // then
        assertEquals(4, map.size());
        assertEquals(map.get("name"), new ObjectExpectationInput("Leo Messi"));
        assertEquals(map.get("email"), new ObjectExpectationInput("leo@messi.com"));
        assertEquals(map.get("height"), new ObjectExpectationInput(170));
        assertEquals(map.get("is_cool"), new ObjectExpectationInput(true));
    }

    @Test
    void mapWithNestedMap() {
        // given
        JazonMap nestedJazonMap = new JazonMap()
                .with("firstname", "Fernando")
                .with("lastname", "Alonso");
        JazonMap jazonMap = new JazonMap()
                .with("driver", nestedJazonMap);

        // when
        Map<String, JsonExpectationInput> map = jazonMap.map();

        // then
        assertEquals(1, map.size());
        assertEquals(map.get("driver"), new ObjectExpectationInput(nestedJazonMap));
    }

    @Test
    void mapWithPredicate() {
        // given
        JazonMap jazonMap = new JazonMap()
                .with("id", (Integer id) -> id > 0);

        // when
        Map<String, JsonExpectationInput> map = jazonMap.map();

        // then
        assertEquals(1, map.size());
        PredicateExpectationInput predicateExpectationInput = (PredicateExpectationInput) map.get("id");
        Predicate<Integer> predicate = predicateExpectationInput.predicate();
        assertFalse(predicate.test(-10));
        assertFalse(predicate.test(-1));
        assertFalse(predicate.test(0));
        assertTrue(predicate.test(1));
        assertTrue(predicate.test(10));
    }

    @Test
    void mapWithAllTheThingsAtOnce() {
        // given
        JazonMap nestedJazonMap = new JazonMap()
                .with("firstname", "Fernando")
                .with("lastname", "Alonso");
        JazonMap jazonMap = new JazonMap()
                .with("name", "Leo Messi")
                .with("email", "leo@messi.com")
                .with("height", 170)
                .with("is_cool", true)
                .with("id", (Integer id) -> id > 0)
                .with("driver", nestedJazonMap);

        // when
        Map<String, JsonExpectationInput> map = jazonMap.map();

        // then
        assertEquals(6, map.size());
        assertEquals(map.get("name"), new ObjectExpectationInput("Leo Messi"));
        assertEquals(map.get("email"), new ObjectExpectationInput("leo@messi.com"));
        assertEquals(map.get("height"), new ObjectExpectationInput(170));
        assertEquals(map.get("is_cool"), new ObjectExpectationInput(true));
        assertEquals(map.get("driver"), new ObjectExpectationInput(nestedJazonMap));

        // and predicate from field "id" is correct
        PredicateExpectationInput predicateExpectationInput = (PredicateExpectationInput) map.get("id");
        Predicate<Integer> predicate = predicateExpectationInput.predicate();
        assertFalse(predicate.test(-10));
        assertFalse(predicate.test(-1));
        assertFalse(predicate.test(0));
        assertTrue(predicate.test(1));
        assertTrue(predicate.test(10));
    }
}
