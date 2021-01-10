package com.zendesk.jazon

import ObjectsActualFactory
import com.zendesk.jazon.expectation.SpockExpectationFactory
import spock.lang.Specification

class MessagesForGroovySpec extends Specification {

    MatcherFactory matcherFactory = new MatcherFactory(
            new SpockExpectationFactory(),
            new ObjectsActualFactory()
    )

    def "predicate expectation: fails"() {
        given:
        Closure closure = { it ==~ "dig.*" }

        when:
        def result = matcherFactory.matcher()
                .expected([a: closure])
                .actual([a: 'rosemary'])
                .match()

        then:
        result.message() == 'Mismatch at path: $.a\nCustom predicate does not match the value.'
        println result.message()
    }
}
