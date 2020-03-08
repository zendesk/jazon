package com.zendesk.jazon.junit;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JazonListTest {

    @Test
    void onlySimpleTypes() {
        // given
        JazonList jazonList = new JazonList("orange", 55, false, 173.50, null);

        // when
        List<JsonExpectationInput> list = jazonList.list();

        // then
        assertEquals(5, list.size());
        assertEquals(new ObjectExpectationInput("orange"), list.get(0));
        assertEquals(new ObjectExpectationInput(55), list.get(1));
        assertEquals(new ObjectExpectationInput(false), list.get(2));
        assertEquals(new ObjectExpectationInput(173.50), list.get(3));
        assertEquals(new ObjectExpectationInput(null), list.get(4));
    }

    @Test
    void nullIsTranslatedToNullPredicate_usingConstructor() {
        // given
        JazonList jazonList = new JazonList(null, null);

        // when
        List<JsonExpectationInput> list = jazonList.list();

        // then
        assertEquals(2, list.size());
        assertEquals(new PredicateExpectationInput<>(null), list.get(0));
    }

    @Test
    void nullIsTranslatedToNullPredicate_usingModifierMethod() {
        // given
        JazonList jazonList = new JazonList().with(null);

        // when
        List<JsonExpectationInput> list = jazonList.list();

        // then
        assertEquals(1, list.size());
        assertEquals(new PredicateExpectationInput<>(null), list.get(0));
    }

    @Test
    void failsForAloneNullInConstructor() {
        assertThrows(NullPointerException.class, () -> new JazonList(null));
    }

    @Test
    void onlyPredicates() {
        // given
        JazonList jazonList = new JazonList((Integer it) -> it > 0, (String s) -> s.matches("re.*"));

        // when
        List<JsonExpectationInput> list = jazonList.list();

        // then
        assertEquals(2, list.size());
        PredicateExpectationInput firstPredicateInput = (PredicateExpectationInput) list.get(0);
        Predicate<Integer> firstPredicate = firstPredicateInput.predicate();
        assertFalse(firstPredicate.test(-10));
        assertFalse(firstPredicate.test(-1));
        assertFalse(firstPredicate.test(0));
        assertTrue(firstPredicate.test(1));
        assertTrue(firstPredicate.test(10));

        PredicateExpectationInput secondPredicateInput = (PredicateExpectationInput) list.get(1);
        Predicate<String> secondPredicate = secondPredicateInput.predicate();
        assertTrue(secondPredicate.test("red"));
        assertTrue(secondPredicate.test("reindeer"));
        assertFalse(secondPredicate.test("black"));
        assertFalse(secondPredicate.test("octopus"));
    }

    @Test
    void nestedMap() {
        // given
        JazonMap robert = new JazonMap()
                .with("firstname", "Robert")
                .with("firstname", "Kubica");
        JazonMap jenson = new JazonMap()
                .with("firstname", "Jenson")
                .with("firstname", "Button");
        JazonList jazonList = new JazonList(robert, jenson);

        // when
        List<JsonExpectationInput> list = jazonList.list();

        // then
        assertEquals(2, list.size());
        assertEquals(new ObjectExpectationInput(robert), list.get(0));
        assertEquals(new ObjectExpectationInput(jenson), list.get(1));
    }

    @Test
    void nestedList() {
        // given
        JazonList drinks = new JazonList("pepsi", "coca cola", "sprite");
        JazonList birds = new JazonList("pigeon", "sparrow");
        JazonList jazonList = new JazonList(drinks, birds);

        // when
        List<JsonExpectationInput> list = jazonList.list();

        // then
        assertEquals(2, list.size());
        assertEquals(new ObjectExpectationInput(drinks), list.get(0));
        assertEquals(new ObjectExpectationInput(birds), list.get(1));
    }

    @Test
    void allThingsAtOnce() {
        // given
        JazonMap nestedMap = new JazonMap()
                .with("firstname", "Robert")
                .with("lastname", "Kubica");
        JazonList drinks = new JazonList("pepsi", "coca cola", "sprite");
        JazonList jazonList = new JazonList()
                .with(150)
                .with(nestedMap)
                .with((Integer it) -> it > 100)
                .with("orange")
                .with(null)
                .with(drinks)
                .with(false);

        // when
        List<JsonExpectationInput> list = jazonList.list();

        // then
        assertEquals(7, list.size());
        assertEquals(new ObjectExpectationInput(150), list.get(0));
        assertEquals(new ObjectExpectationInput(nestedMap), list.get(1));

        PredicateExpectationInput predicateInput = (PredicateExpectationInput) list.get(2);
        Predicate<Integer> firstPredicate = predicateInput.predicate();
        assertFalse(firstPredicate.test(-1));
        assertFalse(firstPredicate.test(0));
        assertFalse(firstPredicate.test(10));
        assertFalse(firstPredicate.test(100));
        assertTrue(firstPredicate.test(101));
        assertTrue(firstPredicate.test(1000));

        assertEquals(new ObjectExpectationInput("orange"), list.get(3));
        assertEquals(new PredicateExpectationInput<>(null), list.get(4));
        assertEquals(new ObjectExpectationInput(drinks), list.get(5));
        assertEquals(new ObjectExpectationInput(false), list.get(6));
    }
}
