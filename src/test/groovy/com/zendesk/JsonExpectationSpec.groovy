package com.zendesk

import spock.lang.Specification

class JsonExpectationSpec extends Specification {

    def "simple number mismatch"() {
        when:
//        def result = new JsonExpectation([a: 123]).match([a: 10])
        def result = new JsonExpectation([a: new PrimitiveValueExpectation(123)]).match([a: 10])

        then:
        !result.ok()
        result.mismatch() == new NumberMismatch(123, 10)
    }
}
