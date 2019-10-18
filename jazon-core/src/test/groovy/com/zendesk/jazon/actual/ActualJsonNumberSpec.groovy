package com.zendesk.jazon.actual

import spock.lang.Specification

class ActualJsonNumberSpec extends Specification {

    def "equals() returns true, hashCode() equals"() {
        given:
        def fooNumber = new ActualJsonNumber(foo as Number)
        def barNumber = new ActualJsonNumber(bar as Number)

        expect:
        fooNumber.equals(barNumber)
        fooNumber.hashCode() == barNumber.hashCode()

        and:
        barNumber.equals(fooNumber)
        barNumber.hashCode() == fooNumber.hashCode()

        where:
        foo                   | bar
        1 as int              | 1 as int
        1 as long             | 1 as long
        1 as long             | 1 as int
        1.1d                  | 1.1d
        1.1f                  | 1.1f
        new BigDecimal("1.1") | new BigDecimal("1.1")
    }

    def "hashCode() differs and equals() returns false"() {
        given:
        def fooNumber = new ActualJsonNumber(foo)
        def barNumber = new ActualJsonNumber(bar)

        expect:
        fooNumber.hashCode() != barNumber.hashCode()
        !fooNumber.equals(barNumber)

        and:
        barNumber.hashCode() != fooNumber.hashCode()
        !barNumber.equals(fooNumber)

        where:
        foo                                             | bar
        1 as int                                        | 2 as int
        1 as long                                       | 2 as long
        1 as int                                        | 2 as long
        1 as long                                       | 2 as int
        ((long) Integer.MAX_VALUE + 1) as long          | Integer.MIN_VALUE as long
        1.2f                                            | 1 as int
        1.2d                                            | 1 as int
        1.2f                                            | 1.4f
        1.2f                                            | 1.4d
        new BigDecimal('1.000000000000001')             | new BigDecimal('1.0000000000000011')
        1.000000000000001d                              | new BigDecimal('1.0000000000000011')
        1.000000000000001f                              | new BigDecimal('1.0000000000000011')
        new BigDecimal('1.000000000000000000000000001') | new BigDecimal('1.0000000000000000000000000011')
        1.1f                                            | new BigDecimal('1.1')    // Yes it does not match. See comments in `ActualJsonNumber.equals()`
        1.1d                                            | new BigDecimal('1.1')    // Yes it does not match. See comments in `ActualJsonNumber.equals()`
        1 as int                                        | 1.0f    // Yes it does not match. See comments in `ActualJsonNumber.equals()`
        1 as int                                        | 1.0d    // Yes it does not match. See comments in `ActualJsonNumber.equals()`
        1 as long                                       | 1.0f    // Yes it does not match. See comments in `ActualJsonNumber.equals()`
        1 as long                                       | 1.0d    // Yes it does not match. See comments in `ActualJsonNumber.equals()`
        1.1f                                            | 1.1d    // Yes it does not match. See comments in `ActualJsonNumber.equals()`
    }

    def "hashCode() equals and equals() returns false"() {
        given:
        def fooNumber = new ActualJsonNumber(foo)
        def barNumber = new ActualJsonNumber(bar)

        expect:
        fooNumber.hashCode() == barNumber.hashCode()
        !fooNumber.equals(barNumber)

        and:
        barNumber.hashCode() == fooNumber.hashCode()
        !barNumber.equals(fooNumber)

        where:
        foo                                    | bar
        ((long) Integer.MAX_VALUE + 1) as long | Integer.MIN_VALUE as int
    }
}
