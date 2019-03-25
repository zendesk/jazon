package com.zendesk.jazon

import com.zendesk.jazon.actual.DefaultActualFactory
import com.zendesk.jazon.expectation.SpockExpectationFactory
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
}
