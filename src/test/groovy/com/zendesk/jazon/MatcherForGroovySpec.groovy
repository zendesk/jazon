package com.zendesk.jazon

import com.zendesk.jazon.actual.DefaultActualFactory
import com.zendesk.jazon.expectation.SpockExpectationFactory
import com.zendesk.jazon.mismatch.PredicateMismatch
import spock.lang.Specification

class MatcherForGroovySpec extends Specification {

    MatcherFactory matcherFactory = new MatcherFactory(
            new SpockExpectationFactory(),
            new DefaultActualFactory()
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

        where:
        stringToMatch << [
                'dog',
                'di',
                'do',
                'dagger',
                'refrigerator',
        ]
    }
}
