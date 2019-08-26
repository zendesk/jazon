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
                .with("nickname", null)
                .with("is_cool", true);

        // when
        Map<String, JsonExpectationInput> map = jazonMap.map();

        // then
        assertEquals(5, map.size());
        assertEquals(new ObjectExpectationInput("Leo Messi"), map.get("name"));
        assertEquals(new ObjectExpectationInput("leo@messi.com"), map.get("email"));
        assertEquals(new ObjectExpectationInput(170), map.get("height"));
        assertEquals(new PredicateExpectationInput<>(null), map.get("nickname"));
        assertEquals(new ObjectExpectationInput(true), map.get("is_cool"));
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
        assertEquals(new ObjectExpectationInput(nestedJazonMap), map.get("driver"));
    }

    @Test
    void mapWithNestedLists() {
        // given
        JazonList drinks = new JazonList("pepsi", "coca cola", "sprite", "fanta");
        JazonList birds = new JazonList("pigeon", "sparrow");
        JazonMap jazonMap = new JazonMap()
                .with("birds", birds)
                .with("drinks", drinks);

        // when
        Map<String, JsonExpectationInput> map = jazonMap.map();

        // then
        assertEquals(2, map.size());
        assertEquals(new ObjectExpectationInput(birds), map.get("birds"));
        assertEquals(new ObjectExpectationInput(drinks), map.get("drinks"));
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
        JazonMap driverMap = new JazonMap()
                .with("firstname", "Fernando")
                .with("lastname", "Alonso");
        JazonList drinksList = new JazonList("pepsi", "coca cola", "sprite", "fanta");
        JazonMap jazonMap = new JazonMap()
                .with("name", "Leo Messi")
                .with("email", "leo@messi.com")
                .with("nickname", null)
                .with("height", 170)
                .with("drinks", drinksList)
                .with("is_cool", true)
                .with("id", (Integer id) -> id > 0)
                .with("driver", driverMap);

        // when
        Map<String, JsonExpectationInput> map = jazonMap.map();

        // then
        assertEquals(8, map.size());
        assertEquals(map.get("name"), new ObjectExpectationInput("Leo Messi"));
        assertEquals(map.get("email"), new ObjectExpectationInput("leo@messi.com"));
        assertEquals(map.get("nickname"), new PredicateExpectationInput<>(null));
        assertEquals(map.get("height"), new ObjectExpectationInput(170));
        assertEquals(map.get("drinks"), new ObjectExpectationInput(drinksList));
        assertEquals(map.get("is_cool"), new ObjectExpectationInput(true));
        assertEquals(map.get("driver"), new ObjectExpectationInput(driverMap));

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
