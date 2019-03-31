package com.zendesk.jazon

import com.zendesk.jazon.actual.ActualFactory
import com.zendesk.jazon.actual.DefaultActualFactory
import com.zendesk.jazon.expectation.DefaultExpectationFactory
import com.zendesk.jazon.expectation.ExpectationFactory
import spock.lang.Specification
import spock.lang.Unroll

class MessagesSpec extends Specification {

    ActualFactory actualFactory = new DefaultActualFactory()
    ExpectationFactory expectationFactory = new DefaultExpectationFactory()
    MatcherFactory matcherFactory = new MatcherFactory(
            expectationFactory,
            actualFactory
    )

    def "object expectation - primitive field value mismatch"() {
        given:
        def expected = [
                a: expectedValue
        ]
        def actual = [
                a: actualValue
        ]

        when:
        def result = match(expected, actual)

        then:
        result.message() == "Mismatch at path: \$.a\nExpected: $expectedInMessage\nActual:   $actualInMessage"
        print result

        where:
        expectedValue | actualValue || expectedInMessage | actualInMessage
        'lance'       | 'vance'     || '"lance"'         | '"vance"'
        120           | 50          || '120'             | '50'
        44.50         | 180.10      || '44.50'           | '180.10'
        true          | false       || 'true'            | 'false'
    }

    def "object expectation - unexpected field"() {
        given:
        def expected = [
                a: 'lance'
        ]
        def actual = [
                a: 'lance',
                b: 'kek'
        ]

        when:
        def result = match(expected, actual)

        then:
        result.message() == 'Mismatch at path: $\nUnexpected field "b" in object.'
        print result
    }

    @Unroll
    def "object expectation - no field (#expectedValue)"() {
        given:
        def expected = [
                a: expectedValue
        ]
        def actual = [
                b: 'strong wind'
        ]

        when:
        def result = match(expected, actual)

        then:
        result.message() == "Mismatch at path: \$\nCould not find expected field (\"a\": $expectedInMessage)"
        print result

        where:
        expectedValue                        | expectedInMessage
        'lance'                              | '"lance"'
        120                                  | '120'
        true                                 | 'true'
        false                                | 'false'
        null                                 | 'null'
        [1, 2, 3]                            | '[1, 2, 3]'
        ['milk', 'sugar', 'flour']           | '["milk", "sugar", "flour"]'
        [1, 2, 3] as Set                     | '[1, 2, 3] (unordered)'
        [name: 'Wayne', surname: 'Rooney']   | '{"name": "Wayne", "surname": "Rooney"}'
        [uno: 1, due: 2, tres: 3, quatro: 4] | '{"uno": 1, "due": 2, ...}'
    }

    def "object expectation: found null instead of #expectedValue"() {
        given:
        def expected = [
                a: 'refrigerator'
        ]
        def actual = [
                a: null
        ]

        when:
        def result = match(expected, actual)

        then:
        result.message() == 'Mismatch at path: \$.a\nFound null. Expected: "refrigerator"'
        print result
    }

    def "object expectation: found something instead of null"() {
        given:
        def expected = [
                a: null
        ]
        def actual = [
                a: 'refrigerator'
        ]

        when:
        def result = match(expected, actual)

        then:
        result.message() == 'Mismatch at path: \$.a\nExpected null. Found: "refrigerator"'
        print result
    }

    def "ordered array expectation: unexpected elements"() {
        given:
        def expected = [
                a: ['red', 'green', 'blue']
        ]
        def actual = [
                a: ['red', 'green', 'blue', 'silver', 'black']
        ]

        when:
        def result = match(expected, actual)

        then:
        result.message() == 'Mismatch at path: \$.a\nArray contains unexpected items: ["silver", "black"]'
        print result
    }

    def "ordered array expectation: lacking elements"() {
        given:
        def expected = [
                a: ['red', 'green', 'blue', 'silver', 'black']
        ]
        def actual = [
                a: ['red', 'green']
        ]

        when:
        def result = match(expected, actual)

        then:
        result.message() == 'Mismatch at path: \$.a\nArray lacks the items: ["blue", "silver", "black"]'
        print result
    }

    MatchResult match(Map expected, Map actual) {
        matcherFactory.matcher()
                .expected(expected)
                .actual(actual)
                .match()
    }

    private void print(MatchResult result) {
        println result.message() + '\n'
    }
}
