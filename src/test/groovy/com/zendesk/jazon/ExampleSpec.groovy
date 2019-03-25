package com.zendesk.jazon

import spock.lang.FailsWith
import spock.lang.Specification

import static com.zendesk.jazon.spock.JazonSpockAdapter.jazon

class ExampleSpec extends Specification {

    def "smoke test"() {
        expect:
        jazon('{"wegorz": "elektryczny"}').matches([wegorz: 'elektryczny'])
    }

    @FailsWith(AssertionError)
    def "failure format test"() {
        expect:
        jazon('{"wegorz": "elektryczny", "ryba": "pila"}').matches([wegorz: 'elektryczny'])
    }

    def "predicate expectation test"() {
        expect:
        jazon('{"wegorz": "elektryczny"}').matches([
                wegorz: { it.startsWith('ele') }
        ])
    }
}
