package com.zendesk.jazon

import com.zendesk.jazon.actual.*
import com.zendesk.jazon.expectation.DefaultExpectationFactory
import com.zendesk.jazon.expectation.ExpectationFactory
import com.zendesk.jazon.expectation.JsonExpectation
import com.zendesk.jazon.mismatch.*
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Predicate

import static com.zendesk.jazon.expectation.Expectations.anyNumberOf

class MatcherSpec extends Specification {

    static ActualFactory actualFactory = new ObjectsActualFactory()
    static ExpectationFactory expectationFactory = new DefaultExpectationFactory()
    static MatcherFactory matcherFactory = new MatcherFactory(
            expectationFactory,
            actualFactory
    )

    @Unroll
    def "primitive value mismatch (expected: #expected, actual: #actual)"() {
        when:
        def result = match([a: expected], [a: actual])

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == primitiveValueMismatch(expected, actual)
        result.mismatch().path() == '$.a'

        where:
        expected                | actual
        123                     | 10
        123                     | 130.1f
        123                     | 1500.13d
        123                     | new BigDecimal("11.05")
        123                     | 12345l
        130.1f                  | 10
        130.1f                  | 133.3f
        130.1f                  | 1500.13d
        130.1f                  | new BigDecimal("11.05")
        130.1f                  | 12345l
        1500.13d                | 10
        1500.13d                | 130.1f
        1500.13d                | 1555.55d
        1500.13d                | new BigDecimal("11.05")
        1500.13d                | 12345l
        new BigDecimal("11.05") | 10
        new BigDecimal("11.05") | 130.1f
        new BigDecimal("11.05") | 1500.13d
        new BigDecimal("11.05") | new BigDecimal("11.11")
        new BigDecimal("11.05") | 12345l
        12345l                  | 10
        12345l                  | 130.1f
        12345l                  | 1500.13d
        12345l                  | new BigDecimal("11.05")
        12345l                  | 1234567l
        'green'                 | 'red'
        true                    | false
        false                   | true
    }

    def "simple primitive type mismatch"() {
        when:
        def result = match(expected, actual)

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new TypeMismatch(mismatchExpectedType, mismatchActualType)
        result.mismatch().path() == '$.a'

        where:
        expected           | actual        | mismatchExpectedType    | mismatchActualType
        [a: 123]           | [a: 'red']    | ActualJsonNumber.class  | ActualJsonString.class
        [a: 123]           | [a: [bb: 10]] | ActualJsonNumber.class  | ActualJsonObject.class
        [a: 123]           | [a: true]     | ActualJsonNumber.class  | ActualJsonBoolean.class
        [a: 123]           | [a: [1, 2]]   | ActualJsonNumber.class  | ActualJsonArray.class
        [a: 'ww']          | [a: 88]       | ActualJsonString.class  | ActualJsonNumber.class
        [a: 'ww']          | [a: [bb: 10]] | ActualJsonString.class  | ActualJsonObject.class
        [a: 'ww']          | [a: true]     | ActualJsonString.class  | ActualJsonBoolean.class
        [a: 'ww']          | [a: [1, 2]]   | ActualJsonString.class  | ActualJsonArray.class
        [a: [bb: 10]]      | [a: 88]       | ActualJsonObject.class  | ActualJsonNumber.class
        [a: [bb: 10]]      | [a: 'red']    | ActualJsonObject.class  | ActualJsonString.class
        [a: [bb: 10]]      | [a: true]     | ActualJsonObject.class  | ActualJsonBoolean.class
        [a: [bb: 10]]      | [a: [1, 2]]   | ActualJsonObject.class  | ActualJsonArray.class
        [a: true]          | [a: 'red']    | ActualJsonBoolean.class | ActualJsonString.class
        [a: true]          | [a: 88]       | ActualJsonBoolean.class | ActualJsonNumber.class
        [a: true]          | [a: [bb: 10]] | ActualJsonBoolean.class | ActualJsonObject.class
        [a: true]          | [a: [1, 2]]   | ActualJsonBoolean.class | ActualJsonArray.class
        [a: [1, 2]]        | [a: 123]      | ActualJsonArray.class   | ActualJsonNumber.class
        [a: [1, 2]]        | [a: 'red']    | ActualJsonArray.class   | ActualJsonString.class
        [a: [1, 2]]        | [a: 88]       | ActualJsonArray.class   | ActualJsonNumber.class
        [a: [1, 2]]        | [a: [bb: 10]] | ActualJsonArray.class   | ActualJsonObject.class
        [a: [1, 2] as Set] | [a: 123]      | ActualJsonArray.class   | ActualJsonNumber.class
        [a: [1, 2] as Set] | [a: 'red']    | ActualJsonArray.class   | ActualJsonString.class
        [a: [1, 2] as Set] | [a: 88]       | ActualJsonArray.class   | ActualJsonNumber.class
        [a: [1, 2] as Set] | [a: [bb: 10]] | ActualJsonArray.class   | ActualJsonObject.class
    }

    @Unroll
    def "finds null instead of primitive value: #expected"() {
        when:
        def result = match([a: expected], [a: null])

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new NullMismatch(expectationFactory.expectation(expected))
        result.mismatch().path() == '$.a'

        where:
        expected                | expectedType
        123                     | ActualJsonNumber
        130.1f                  | ActualJsonNumber
        1500.13d                | ActualJsonNumber
        new BigDecimal("11.05") | ActualJsonNumber
        12345l                  | ActualJsonNumber
        "sting"                 | ActualJsonString
        true                    | ActualJsonBoolean
    }

    @Unroll
    def "mismatch in object field (expected: #expectedFieldValue, actual: #actualFieldValue)"() {
        given:
        def expected = [
                a: 103,
                b: expectedFieldValue
        ]
        def actual = [
                a: 103,
                b: actualFieldValue,
        ]

        when:
        def result = match(expected, actual)

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == foundMismatch
        result.mismatch().path() == mismatchPath

        where:
        expectedFieldValue | actualFieldValue || mismatchPath | foundMismatch
        'vegetable'        | 'meat'           || '$.b'        | primitiveValueMismatch('vegetable', 'meat')
        'vegetable'        | null             || '$.b'        | new NullMismatch<>(expectationFactory.expectation('vegetable'))
        'vegetable'        | 150              || '$.b'        | new TypeMismatch(ActualJsonString, ActualJsonNumber)
        77                 | 'rosemary'       || '$.b'        | new TypeMismatch(ActualJsonNumber, ActualJsonString)
        []                 | 'car'            || '$.b'        | new TypeMismatch(ActualJsonArray, ActualJsonString)
        [20, 30]           | [20, 77]         || '$.b.1'      | primitiveValueMismatch(30, 77)
    }

    def "catches lacking field in Object"() {
        given:
        def expected = [
                a: 103,
                b: 'some value',
        ]

        when:
        def result = match(expected, actual)

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new NoFieldMismatch(
                'b',
                expectationFactory.expectation('some value')
        )
        result.mismatch().path() == '$'

        where:
        actual << [
                [a: 103],
                [a: 103, c: 'car']
        ]
    }

    @Unroll
    def "catches unexpected field in Object: #unexpectedFieldValue"() {
        given:
        def expected = [
                a: 103,
                c: 'Chicago',
        ]
        def actual = [
                a: 103,
                b: unexpectedFieldValue,
                c: 'Chicago',
        ]

        when:
        def result = match(expected, actual)

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new UnexpectedFieldMismatch('b')
        result.mismatch().path() == '$'

        where:
        unexpectedFieldValue    | unexpectedFieldType
        'act of vandalism'      | ActualJsonString
        123                     | ActualJsonNumber
        1999l                   | ActualJsonNumber
        20.14f                  | ActualJsonNumber
        44.999d                 | ActualJsonNumber
        new BigDecimal("80.92") | ActualJsonNumber
        [a: 1, b: 'blue']       | ActualJsonObject
        null                    | ActualJsonNull
        [5, 4, 3]               | ActualJsonArray
        true                    | ActualJsonBoolean
        false                   | ActualJsonBoolean
    }

    @Unroll
    def "object expectation - type mismatch for #actualType"() {
        given:
        def theObject = [
                id: 1,
                name: "Leo",
                nationality: "Argentinian"
        ]
        def expected = [a: theObject]
        def actual = [a: actualValue]

        when:
        def result = match(expected, actual)

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new TypeMismatch(ActualJsonObject, actualType)
        result.mismatch().path() == '$.a'

        where:
        actualValue | actualType
        true        | ActualJsonBoolean
        130         | ActualJsonNumber
        'orange'    | ActualJsonString
        [1, 2, 3]   | ActualJsonArray
    }

    @Unroll
    def "ordered list expectation - exact element mismatch"() {
        when:
        def result = match([a: expected], [a: actual])

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == elementMismatch
        result.mismatch().path() == '$.a.' + elementIndex

        where:
        expected     | actual       || elementIndex | elementMismatch
        [1, 2, 3]    | [3, 2, 1]    || 0            | primitiveValueMismatch(1, 3)
        [1, 2, 3]    | [1, 7, 3]    || 1            | primitiveValueMismatch(2, 7)
        [1, 2, true] | [1, 2, 3]    || 2            | new TypeMismatch(ActualJsonBoolean, ActualJsonNumber)
        [1, 2, 3]    | [1, 2, true] || 2            | new TypeMismatch(ActualJsonNumber, ActualJsonBoolean)
        [1, null, 3] | [1, 2, 3]    || 1            | new NotNullMismatch(new ActualJsonNumber(2))
        [1, 2, 3]    | [1, null, 3] || 1            | new NullMismatch<>(expectationFactory.expectation(2))
        [1, 2, 3]    | [1, 2, 4, 5] || 2            | primitiveValueMismatch(3, 4)
    }

    @Unroll
    def "ordered list expectation - lacking elements (expected: #expected, actual: #actual)"() {
        when:
        def result = match([a: expected], [a: actual])

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new ArrayLackingElementsMismatch(
                lackingElements.collect(expectationFactory.&expectation)
        )
        result.mismatch().path() == '$.a'

        where:
        expected                  | actual          || lackingElements
        [1, 2, 3]                 | [1, 2]          || [3]
        [1, 2, 'lalala']          | [1, 2]          || ['lalala']
        [1, 2, 'lalala', 5, 6, 7] | [1, 2]          || ['lalala', 5, 6, 7]
        [1, 2, null, 5, 6, 7]     | [1, 2, null, 5] || [6, 7]
        [1, null]                 | [1]             || [null]
        [9]                       | []              || [9]
        [null]                    | []              || [null]
        [null, 'car', 17]         | []              || [null, 'car', 17]
    }

    def "ordered list expectation - unexpected elements"() {
        when:
        def result = match([a: expected], [a: actual])

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new ArrayUnexpectedElementsMismatch(
                unexpectedElements.collect(actualFactory.&actual)
        )
        result.mismatch().path() == '$.a'

        where:
        expected       | actual                || unexpectedElements
        [1, 2]         | [1, 2, 3]             || [3]
        []             | [1, 2, 3]             || [1, 2, 3]
        ['carpet']     | ['carpet', 'fur']     || ['fur']
        []             | [null]                || [null]
        []             | [null, null]          || [null, null]
        [1, 2]         | [1, 2, null]          || [null]
        [true, 'bike'] | [true, 'bike', false] || [false]
    }

    @Unroll
    def "ordered list expectation - type mismatch for #actualType"() {
        given:
        def theArray = ['white bear', 'seal', 'penguin']
        def expected = [a: theArray]
        def actual = [a: actualValue]

        when:
        def result = match(expected, actual)

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new TypeMismatch(ActualJsonArray, actualType)
        result.mismatch().path() == '$.a'

        where:
        actualValue | actualType
        true        | ActualJsonBoolean
        130         | ActualJsonNumber
        'orange'    | ActualJsonString
        [a: 44]     | ActualJsonObject
    }

    def "unordered list expectation: success"() {
        when:
        def result = match([a: expected], [a: actual])

        then:
        result.ok()

        where:
        expected                         | actual
        [1, 2, 3] as Set                 | [1, 3, 2]
        [1, 2, 3] as Set                 | [3, 1, 2]
        [1, 2, 'lalala'] as Set          | ['lalala', 1, 2]
        [1, 2, 'lalala', 5, 6, 7] as Set | [6, 7, 2, 5, 'lalala', 1]
    }

    @Unroll
    def "unordered list expectation: lacking elements (actual: #actual)"() {
        when:
        def result = match([a: expected], [a: actual])

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new ArrayLackingElementsMismatch(
                lackingElements.collect(expectationFactory.&expectation) as Set
        )
        result.mismatch().path() == '$.a'

        where:
        expected                         | actual                 || lackingElements
        [1, 2, 3] as Set                 | [1, 3]                 || [2]
        [1, 2, 3] as Set                 | [1, 3, 1]              || [2]
        [1, 2, 3] as Set                 | [3, 1, 1, 88]          || [2]
        [1, 2, 3] as Set                 | [3, 1, 3, 1]           || [2]
        [1, 2, 'lalala'] as Set          | ['lalala', 11, 2]      || [1]
        [1, 2, 'lalala', 5, 6, 7] as Set | [6, 7, 2, 5, 'rob', 1] || ['lalala']
        [3, 4, 2, 1] as Set              | [11, 2, 3, 55]         || [1, 4]
        [3, 4, 2, 1] as Set              | [1]                    || [2, 3, 4]
        [3, 4, 2, 1] as Set              | [2]                    || [1, 3, 4]
        [3, 4, 2, 1] as Set              | [3]                    || [1, 2, 4]
        [3, 4, 2, 1] as Set              | [4]                    || [1, 2, 3]
    }

    def "unordered list expectation: unexpected elements"() {
        when:
        def result = match([a: expected], [a: actual])

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new ArrayUnexpectedElementsMismatch(
                unexpectedElements.collect(actualFactory.&actual)
        )
        result.mismatch().path() == '$.a'

        where:
        expected                      | actual                     || unexpectedElements
        [1, 2, 3] as Set              | [1, 3, 2, 8]               || [8]
        [1, 2, 3] as Set              | [1, 3, 2, 'sushi']         || ['sushi']
        [1, 2, 3] as Set              | [1, 'sushi', 2, 3]         || ['sushi']
        [1, 2, 3] as Set              | [1, 3, 2, null]            || [null]
        ['what', 'is', 'love'] as Set | ['love', 'is', 10, 'what'] || [10]
    }

    @Unroll
    def "unordered list expectation - type mismatch for #actualType"() {
        given:
        def theSet = ['white bear', 'seal', 'penguin'] as Set
        def expected = [a: theSet]
        def actual = [a: actualValue]

        when:
        def result = match(expected, actual)

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new TypeMismatch(ActualJsonArray, actualType)
        result.mismatch().path() == '$.a'

        where:
        actualValue | actualType
        true        | ActualJsonBoolean
        130         | ActualJsonNumber
        'orange'    | ActualJsonString
        [a: 44]     | ActualJsonObject
    }

    def "unordered list expectation: fails for unsupported expectation types"() {
        given:
        def unsupportedExpectation = [1, 2, 3] as Set
        def unorderedArrayExpectationWrapping = ['fish', 'chips', unsupportedExpectation] as Set
        def theWholeExpectation = [a: unorderedArrayExpectationWrapping]

        when:
        expectationFactory.expectation(theWholeExpectation)

        then:
        thrown(IllegalStateException)
    }

    @Unroll
    def "array each element expectation: success"() {
        expect:
        match([a: anyNumberOf(expected)], [a: actual]).success()

        where:
        expected                                       | actual
        '1'                                            | []
        '1'                                            | ['1']
        '1'                                            | ['1', '1']
        true                                           | [true]
        2                                              | [2]
        { it -> it > 5 } as Predicate<Integer>  | [6, 7, 8]
    }

    @Unroll
    def "array each element expectation - element mismatch"() {
        when:
        def result = match([a: anyNumberOf(expected)], [a: actual])

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == elementMismatch
        result.mismatch().path() == '$.a.' + elementIndex

        where:
        expected                       | actual          || elementIndex | elementMismatch                                       | _
        1                              | [1, 3, 1]       || 1            | primitiveValueMismatch(1, 3)                          | _
        1                              | [1, 1, true]    || 2            | new TypeMismatch(ActualJsonNumber, ActualJsonBoolean) | _
        true                           | [true, 1, true] || 1            | new TypeMismatch(ActualJsonBoolean, ActualJsonNumber) | _
        1                              | [1, null, 1]    || 1            | new NullMismatch<>(expectationFactory.expectation(1)) | _
        ({ it -> it > 3 }
                as Predicate<Integer>) | [4, 5, 2]       || 2            | PredicateMismatch.INSTANCE                            | _
    }

    def "null expectation: fails for any present value"() {
        when:
        def result = match([a: null], [a: actual])

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == new NotNullMismatch(actualFactory.actual(actual))
        result.mismatch().path() == '$.a'

        where:
        actual << [
                'something',
                10,
                130.1f,
                1555.55d,
                new BigDecimal("11.05"),
                12345l,
                [x: 123],
                [1, 2, 3],
                true,
                false
        ]
    }

    def "null expectation: succeeds for null"() {
        when:
        def result = match([a: null], [a: null])

        then:
        result.ok()
    }

    def "any expectation can be root expectation"() {
        when:
        def result = matcherFactory.matcher()
                .expected(expected)
                .actual(actual)
                .match()

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == mismatch
        result.mismatch().path() == path

        where:
        expected         | actual       || path  | mismatch
        [1, 2, 3]        | [1, 88, 3]   || '$.1' | primitiveValueMismatch(2, 88)
        [1, 2, 3] as Set | [3, 1, 99]   || '$'   | new ArrayLackingElementsMismatch([expectation(2)] as Set)
        'medicine'       | 'drug'       || '$'   | primitiveValueMismatch('medicine', 'drug')
        100              | 99           || '$'   | primitiveValueMismatch(100, 99)
        true             | false        || '$'   | primitiveValueMismatch(true, false)
        null             | 'vegetables' || '$'   | new NotNullMismatch(actualFactory.actual('vegetables'))
        [a: 1]           | [a: 9]       || '$.a' | primitiveValueMismatch(1, 9)
    }

    private static MatchResult match(Map expected, Map actual) {
        matcherFactory.matcher()
                .expected(expected)
                .actual(actual)
                .match()
    }

    private static PrimitiveValueMismatch primitiveValueMismatch(def expected, def actual) {
        return new PrimitiveValueMismatch(actualFactory.actual(expected), actualFactory.actual(actual))
    }

    private static JsonExpectation expectation(Object object) {
        expectationFactory.expectation(object)
    }
}
