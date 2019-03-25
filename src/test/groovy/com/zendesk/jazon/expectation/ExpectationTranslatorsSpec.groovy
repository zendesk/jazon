package com.zendesk.jazon.expectation

import com.zendesk.jazon.actual.ActualJsonNumber
import com.zendesk.jazon.actual.ActualJsonString
import spock.lang.Specification
import spock.lang.Unroll

class ExpectationTranslatorsSpec extends Specification {

    ExpectationFactory factory = new DefaultExpectationFactory()

    @Unroll
    def "simple object from #inputMap"() {
        when:
        ObjectExpectation expectation = factory.expectation(inputMap)

        then:
        expectation == expectedOutput

        where:
        [inputMap, expectedOutput] << [
                [
                        [a: 123],
                        new ObjectExpectation([
                                a: new PrimitiveValueExpectation<>(123, ActualJsonNumber)
                        ])
                ],
                [
                        [a: 'ale beka'],
                        new ObjectExpectation([
                                a: new PrimitiveValueExpectation<>('ale beka', ActualJsonString)
                        ])
                ],
        ]
    }
}
