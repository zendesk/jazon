package com.zendesk.jazon.expectation

import com.zendesk.jazon.actual.ActualJsonNumber
import com.zendesk.jazon.actual.ActualJsonString
import spock.lang.Specification
import spock.lang.Unroll

import static com.zendesk.jazon.expectation.ExpectationTranslators.objectExpectation

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
