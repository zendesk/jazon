package com.zendesk

import spock.lang.Specification

class FacadeExpectationSpec extends Specification {

    def "simple number mismatch"() {
        when:
        def result = new FacadeExpectation([a: 123]).match([a: 10])

        then:
        !result.ok()
        result.mismatch() == new PrimitiveValueMismatch(123, 10)
    }

    def "simple string mismatch"() {
        when:
        def result = new FacadeExpectation([a: 'something serious']).match([a: 'lol'])

        then:
        !result.ok()
        result.mismatch() == new PrimitiveValueMismatch('something serious', 'lol')
    }
}
