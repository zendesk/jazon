package com.zendesk.jazon

import com.zendesk.jazon.actual.factory.GsonActualFactory
import com.zendesk.jazon.expectation.DefaultTranslators
import com.zendesk.jazon.expectation.JazonTypesTranslators
import com.zendesk.jazon.expectation.SpockTranslators
import com.zendesk.jazon.expectation.TranslatorFacade
import com.zendesk.jazon.mismatch.PredicateMismatch
import spock.lang.Specification

import static com.zendesk.jazon.expectation.Expectations.anyNumberOf
import static groovy.json.JsonOutput.toJson

class MatcherForGroovySpec extends Specification {

    MatcherFactory matcherFactory = new MatcherFactory(
            new TranslatorFacade(
                    DefaultTranslators.translators() + JazonTypesTranslators.translators() + SpockTranslators.translators()
            ),
            new GsonActualFactory()
    )

    def "predicate expectation: succeeds"() {
        given:
        Closure closure = { it ==~ "dig.*" }

        when:
        def result = matcherFactory.matcher()
                .expected([a: closure])
                .actual(toJson([a: stringToMatch]))
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
                .actual(toJson([a: stringToMatch]))
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
                .actual(toJson([a: 'this is interpolated string with 123']))
                .match()

        then:
        result.ok()
    }

    def "predicate expectation for array: fails"() {
        when:
        def result = matcherFactory.matcher()
                .expected([a: predicate])
                .actual(toJson([a: ['chips', 'ketchup', 'fish']]))
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
                .actual(toJson([a: ['chips', 'ketchup', 'fish']]))
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
                .actual(toJson([a: [name: 'tomato', color: 'red']]))
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
                .actual(toJson([a: [name: 'tomato', color: 'red']]))
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
            .actual(toJson([a: [6, 7, 8, 9, 0]]))
            .match()

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == PredicateMismatch.INSTANCE
        result.mismatch().path() == '$.a.4'
    }
}
