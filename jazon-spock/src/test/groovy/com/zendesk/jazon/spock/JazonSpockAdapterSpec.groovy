package com.zendesk.jazon.spock


import spock.lang.Specification

import static com.zendesk.jazon.spock.JazonSpockAdapter.jazon

class JazonSpockAdapterSpec extends Specification {

    def 'formats the error message well'() {
        when:
        jazon('["platypus", "narwhal"]')
                .matches(['platypus', 'penguin'])

        then:
        AssertionError assertionError = thrown()
        assertionError.toString() == EXPECTED_ERROR_MESSAGE
    }

    def 'smoke test for all types at once'() {
        expect:
        jazon(JSON_WITH_ALL_TYPES).matches([
                nested_object: [
                    some_number: 123
                ],
                nested_array: ['red', 'green', 'blue'],
                some_boolean: true,
                a_null: null,
                some_string: 'whatever',
                some_integer: 123,
                some_long: 123456789012345678,
                some_double: 99.77
        ])
    }

    private static final String EXPECTED_ERROR_MESSAGE = '''java.lang.AssertionError: 
-----------------------------------
JSON MISMATCH:
Mismatch at path: $.1
Expected: "penguin"
Actual:   "narwhal"
-----------------------------------
'''
    private static final String JSON_WITH_ALL_TYPES = '''
{
    "nested_object": {
        "some_number": 123
    },
    "nested_array": ["red", "green", "blue"],
    "some_boolean": true,
    "a_null": null,
    "some_string": "whatever",
    "some_integer": 123,
    "some_long": 123456789012345678,
    "some_double": 99.77 
}
'''
}
