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

    def "simple primitive type mismatch"() {
        when:
        def result = new FacadeExpectation(expected).match(actual)

        then:
        !result.ok()
        result.mismatch() == new TypeMismatch(mismatchExpectedType, mismatchActualType)

        where:
        expected      | actual        | mismatchExpectedType   | mismatchActualType
        [a: 123]      | [a: 'lol']    | ActualJsonNumber.class | ActualJsonString.class
        [a: 'ww']     | [a: 88]       | ActualJsonString.class | ActualJsonNumber.class
        [a: 123]      | [a: [bb: 10]] | ActualJsonNumber.class | ActualJsonObject.class
        [a: 'ww']     | [a: [bb: 10]] | ActualJsonString.class | ActualJsonObject.class
        [a: [bb: 10]] | [a: 88]       | ActualJsonObject.class | ActualJsonNumber.class
        [a: [bb: 10]] | [a: 'lol']    | ActualJsonObject.class | ActualJsonString.class
    }
}
