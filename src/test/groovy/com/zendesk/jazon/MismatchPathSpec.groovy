package com.zendesk.jazon


import com.zendesk.jazon.actual.ActualFactory
import com.zendesk.jazon.actual.DefaultActualFactory
import com.zendesk.jazon.expectation.DefaultExpectationFactory
import com.zendesk.jazon.expectation.ExpectationFactory
import com.zendesk.jazon.mismatch.ArrayElementMismatch
import com.zendesk.jazon.mismatch.PrimitiveValueMismatch
import spock.lang.Specification

class MismatchPathSpec extends Specification {

    ActualFactory actualFactory = new DefaultActualFactory()
    ExpectationFactory expectationFactory = new DefaultExpectationFactory()
    MatcherFactory matcherFactory = new MatcherFactory(
            expectationFactory,
            actualFactory
    )

    def "path to mismatch is returned"() {
        given:
        def expected = [
                data: [
                        [key: 111, values: [a: 1, b: 2]],
                        [key: 111, values: [a: 2, b: 4]],
                        [key: 111, values: [a: 3, b: 8]],
                        [key: 111, values: [a: 4, b: 16]],
                ]
        ]
        def actual = [
                data: [
                        [key: 111, values: [a: 1, b: 2]],
                        [key: 111, values: [a: 2, b: 4]],
                        [key: 111, values: [a: 3, b: 99]],
                        [key: 111, values: [a: 4, b: 16]],
                ]
        ]

        when:
        def result = match(expected, actual)

        then:
        !result.ok()
        result.mismatch() == new ArrayElementMismatch(2, new PrimitiveValueMismatch(8, 99))
    }

    MatchResult match(Map expected, Map actual) {
        matcherFactory.matcher()
                .expected(expected)
                .actual(actual)
                .match()
    }
}
