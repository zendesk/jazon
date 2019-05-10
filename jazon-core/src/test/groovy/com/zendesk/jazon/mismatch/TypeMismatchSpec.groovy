package com.zendesk.jazon.mismatch

import com.zendesk.jazon.actual.ActualJsonArray
import com.zendesk.jazon.actual.ActualJsonBoolean
import com.zendesk.jazon.actual.ActualJsonNumber
import com.zendesk.jazon.actual.ActualJsonObject
import com.zendesk.jazon.actual.ActualJsonString
import spock.lang.Specification

class TypeMismatchSpec extends Specification {

    def "displays good message"() {
        given:
        def mismatch = new TypeMismatch(expectedType, actualType)

        expect:
        mismatch.message() == "Expected type: $expectedTypeAsString\nActual type:   $actualTypeAsString"
        println mismatch.message() + '\n'

        where:
        expectedType      | actualType       || expectedTypeAsString | actualTypeAsString
        ActualJsonObject  | ActualJsonArray  || 'Object'             | 'Array'
        ActualJsonArray   | ActualJsonNumber || 'Array'              | 'Number'
        ActualJsonNumber  | ActualJsonString || 'Number'             | 'String'
        ActualJsonBoolean | ActualJsonObject || 'Boolean'            | 'Object'
    }
}
