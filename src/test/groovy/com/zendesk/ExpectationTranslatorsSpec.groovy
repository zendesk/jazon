package com.zendesk

import spock.lang.Specification
import spock.lang.Unroll

import static com.zendesk.ExpectationTranslators.*

class ExpectationTranslatorsSpec extends Specification {

    @Unroll
    def "simple object from #inputMap"() {
        when:
        ObjectExpectation expectation = objectExpectation(inputMap)

        then:
        expectation == expectedOutput

        where:
        [inputMap, expectedOutput] << [
                [
                        [a: 123],
                        new ObjectExpectation([
                                a: new PrimitiveValueExpectation<>(123)
                        ])
                ],
                [
                        [a: 'ale beka'],
                        new ObjectExpectation([
                                a: new PrimitiveValueExpectation<>('ale beka')
                        ])
                ],
        ]
    }
}
