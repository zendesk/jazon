package com.zendesk.jazon

import ObjectsActualFactory
import com.zendesk.jazon.expectation.SpockExpectationFactory
import com.zendesk.jazon.mismatch.PredicateMismatch
import spock.lang.Specification

import static com.zendesk.jazon.expectation.Expectations.anyNumberOf

class MatcherForGroovySpec extends Specification {

    MatcherFactory matcherFactory = new MatcherFactory(
            new SpockExpectationFactory(),
            new ObjectsActualFactory()
    )

    def "predicate expectation: succeeds"() {
        given:
        Closure closure = { it ==~ "dig.*" }

        when:
        def result = matcherFactory.matcher()
                .expected([a: closure])
                .actual([a: stringToMatch])
                .match()

        then:
        result.ok()

        where:
        stringToMatch << [
                'dig',
                'digger',
                'digging',
                'digs',
        ]
    }

    def "predicate expectation: fails"() {
        given:
        Closure closure = { it ==~ "dig.*" }

        when:
        def result = matcherFactory.matcher()
                .expected([a: closure])
                .actual([a: stringToMatch])
                .match()

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == PredicateMismatch.INSTANCE
        result.mismatch().path() == '$.a'

        where:
        stringToMatch << [
                'dog',
                'di',
                'do',
                'dagger',
                'refrigerator',
        ]
    }

    def "predicate expectation can be root"() {
        when:
        def result = matcherFactory.matcher()
                .expected({ it ==~ 'dig.*' })
                .actual('refrigerator')
                .match()

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == PredicateMismatch.INSTANCE
        result.mismatch().path() == '$'
    }

    def "Groovy's GString works well"() {
        given:
        def someVariable = 123
        def gstring = "this is interpolated string with $someVariable"

        when:
        def result = matcherFactory.matcher()
                .expected([a: gstring])
                .actual([a: 'this is interpolated string with 123'])
                .match()

        then:
        result.ok()
    }

    def "predicate expectation for array: fails"() {
        when:
        def result = matcherFactory.matcher()
                .expected([a: predicate])
                .actual([a: ['chips', 'ketchup', 'fish']])
                .match()

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == PredicateMismatch.INSTANCE
        result.mismatch().path() == '$.a'

        where:
        predicate << [
                { it.size() == 9 },
                { it[1] == 'fish' },
        ]
    }

    def "predicate expectation for array succeeds"() {
        when:
        def result = matcherFactory.matcher()
                .expected([a: predicate])
                .actual([a: ['chips', 'ketchup', 'fish']])
                .match()

        then:
        result.ok()

        where:
        predicate << [
                { it.size() == 3 },
                { it[1] == 'ketchup' },
        ]
    }

    def "predicate expectation for object: fails"() {
        when:
        def result = matcherFactory.matcher()
                .expected([a: predicate])
                .actual([a: [name: 'tomato', color: 'red']])
                .match()

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == PredicateMismatch.INSTANCE
        result.mismatch().path() == '$.a'

        where:
        predicate << [
                { it.size() == 17 },
                { it.name == 'cucumber' },
        ]
    }

    def "predicate expectation for object succeeds"() {
        when:
        def result = matcherFactory.matcher()
                .expected([a: predicate])
                .actual([a: [name: 'tomato', color: 'red']])
                .match()

        then:
        result.ok()

        where:
        predicate << [
                { it.name == 'tomato' },
                { it.color == 'red' },
        ]
    }

    def "array each element expectation works correctly with lambda-style expectation"() {
        when:
        def result = matcherFactory.matcher()
            .expected([a: anyNumberOf {it -> it > 5}])
            .actual([a: [6, 7, 8, 9, 0]])
            .match()

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == PredicateMismatch.INSTANCE
        result.mismatch().path() == '$.a.4'
    }
}
