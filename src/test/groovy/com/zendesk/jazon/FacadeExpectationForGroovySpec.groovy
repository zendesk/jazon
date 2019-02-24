package com.zendesk.jazon

import com.zendesk.jazon.actual.DefaultActualFactory
import com.zendesk.jazon.expectation.SpockExpectationFactory
import spock.lang.Specification

class FacadeExpectationForGroovySpec extends Specification {

    FacadeExpectationFactory facadeExpectationFactory = new FacadeExpectationFactory(
            new SpockExpectationFactory(),
            new DefaultActualFactory()
    )

    def "predicate expectation: succeeds"() {
        given:
        Closure closure = { it ==~ "dig.*" }
        def expectation = facadeExpectationFactory.facadeExpectation([a: closure])

        when:
        def result = expectation.match([a: stringToMatch])

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
