package com.getbase.jazon

import com.google.common.collect.Lists
import groovy.json.JsonSlurper

import static java.lang.String.join

class Jazon {
    private final Object actual;

    static Jazon jazon(String json) {
        return new Jazon(new JsonSlurper().parseText(json))
    }

    private Jazon(Object object) {
        this.actual = object;
    }

    static Jazon jazonFromList(actual) {
        return new Jazon(actual)
    }

    def sorted(String collectionPath, String key) {
        def comparator = {a, b -> a.data[key] <=> b.data[key]}
        List list = collectionPath.split('\\.').inject(actual) { map, path -> map[path]}
        list.sort(true, comparator)
        return this
    }

    def matches(Object object) {
        MatchResult matchResult = matches(object, this.actual)
        assert matchResult.result == MatchResult.Result.SUCCESS : matchResult.context
        return this
    }

    def containsOnly(List expected) {
        MatchResult matchResult = containsOnly(expected, this.actual);
        assert matchResult.result == MatchResult.Result.SUCCESS : matchResult.context
        return this
    }

    def static matches(Object object, actual) {
        MatchResult matchResult = MatchResult.SUCCESS;
        try {
            anythingMatchesAnything(actual, object, [])
        } catch (NotMatchedException e) {
            matchResult = e.matchResult;
        }
        return matchResult
    }

    def static containsOnly(List expected, Object actual) {
        checkForType({it instanceof List}, actual, expected, [])
        def (List expectedButNotFoundInActual, List notExpectedInActualButFound) = tryMatchExpectedToActual(actual, expected)
        if (!expectedButNotFoundInActual.isEmpty() || !notExpectedInActualButFound.isEmpty()) {
            return new MatchResult(MatchResult.Result.FAILURE, failContextFromContainsOnly(expected, actual, expectedButNotFoundInActual, notExpectedInActualButFound))
        }
        return MatchResult.SUCCESS
    }

    private static List tryMatchExpectedToActual(actual, List expected) {
        List expectedButNotFound = []
        List actualList = Lists.newArrayList(actual)
        for (int expectedIndex = 0; expectedIndex < expected.size(); expectedIndex++) {
            for (int actualIndex = 0; actualIndex < actualList.size(); actualIndex++) {
                def actualItem = actualList[actualIndex]
                def result = matches(expected[expectedIndex], actualItem)
                if (result.result == MatchResult.Result.SUCCESS) {
                    //expected[index] matches actual[index], removing from actual and break from inner loop
                    actualList.remove(actualItem)
                    break
                } else if (actualIndex == actualList.size() - 1) {
                    //this is last element, it means that expected is not matched to any from actual
                    expectedButNotFound += expected[expectedIndex]
                }
            }
        }
        [expectedButNotFound, actualList]
    }

    private static def MatchResult anythingMatchesAnything(def actual, def expected, List path) {
        if (expected instanceof Map) {
            checkForType({it instanceof Map}, actual, expected, path)
            mapMatchesMap((Map) actual, expected, path)
        } else if (expected instanceof List) {
            checkForType({it instanceof List}, actual, expected, path)
            listMatchesList((List) actual, expected, path)
        } else if (expected instanceof Set) {
            checkForType({it instanceof List}, actual, expected, path)
            listMatchesSet((List) actual, expected, path)
        } else if (expected instanceof Number) {
            checkForType({it instanceof Number}, actual, expected, path)
            fieldMatchesField(actual, expected, path)
        } else if (expected instanceof String) {
            checkForType({it instanceof String}, actual, expected, path)
            fieldMatchesField(actual, expected, path)
        } else if (expected instanceof Boolean) {
            checkForType({it instanceof Boolean}, actual, expected, path)
            fieldMatchesField(actual, expected, path)
        } else if (expected instanceof Closure) {
            checkForClosure(actual, expected, path)
        } else {
            fieldMatchesField(actual, expected, path)
        }
        return MatchResult.SUCCESS;
    }

    static def void checkForType(Closure closure, Object actual, Object expected, List path) {
        if (!closure(actual)) {
            throw new NotMatchedException(new MatchResult(MatchResult.Result.FAILURE, failContext(path, actual, expected, "actual insntaceOf ${expected.getClass().getSimpleName()}")))
        }
    }

    private static def mapMatchesMap(Map actual, Map expected, List path) {
        def unwantedKeys = actual.keySet() - expected.keySet()
        if (!unwantedKeys.isEmpty()) {
            throw new NotMatchedException(new MatchResult(MatchResult.Result.FAILURE, failContext(path, actual, expected, "actual.keySet().minus(expected.keySet()).isNotEmpty() (Unexpected fields ${unwantedKeys} in actual)")))
        }

        def unwantedKeys2 = expected.keySet() - actual.keySet()
        if (!unwantedKeys2.isEmpty()) {
            throw new NotMatchedException(new MatchResult(MatchResult.Result.FAILURE, failContext(path, actual, expected, "expected.keySet().minus(actual.keySet()).isNotEmpty() (Unexpected fields ${unwantedKeys2} in actual)")))
        }

        expected.each {
            anythingMatchesAnything(actual[it.key], it.value, path + it.key)
        }
    }

    private static def listMatchesList(List actual, List expected, List path) {
        if (actual.size() != expected.size()) {
            throw new NotMatchedException(new MatchResult(MatchResult.Result.FAILURE, failContext(path, actual, expected, "actual.size() == expected.size()")))
        }
        for (int i = 0; i < actual.size(); i++) {
            anythingMatchesAnything(actual[i], expected[i], path + String.valueOf(i))
        }
    }

    private static def listMatchesSet(List actual, Set expected, List path) {
        if (actual.size() != expected.size()) {
            throw new NotMatchedException(new MatchResult(MatchResult.Result.FAILURE, failContext(path, actual, expected, "actual.size() == expected.size()")))
        }
        def match = containsOnly(expected as List, actual)
        if (match.result != MatchResult.Result.SUCCESS) {
            throw new NotMatchedException(match)
        }
    }

    private static def fieldMatchesField(Object actual, Object expected, List path) {
        if(actual != expected) {
            throw new NotMatchedException(new MatchResult(MatchResult.Result.FAILURE, failContext(path, actual, expected, "actual == expected")))
        }
    }

    private static def checkForClosure(Object actual, Closure closure, List path) {
        if(!closure(actual)) {
            throw new NotMatchedException(new MatchResult(MatchResult.Result.FAILURE, failContext(path, actual, closure, closure.toString())))
        }
    }

    private static String failContext(List path, Object actual, Object expected, String expression) {
        return "Mismatch at path ${join('.', path)} with types (actual: ${actual.getClass()}, expected: ${expected.getClass()})\n" +
        "Expression: ${expression}\n" +
        "Actual   : ${actual}\n" +
        "Expected : ${expected}\n"
    }

    static def failContextFromContainsOnly(List expected, Object actual, List expectedButNotFound, notExpectedInActual) {
        return "Lists do not match\n" +
                "Expected but not found in actual: ${expectedButNotFound}\n" +
                "Not expected but found in actual: ${notExpectedInActual}\n\n" +
                "Expected was: ${expected}\n" +
                "Actual was: ${actual}"
    }
}