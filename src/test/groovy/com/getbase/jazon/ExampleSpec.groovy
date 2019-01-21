package com.getbase.jazon

import spock.lang.Ignore
import spock.lang.Specification

import static com.zendesk.jazon.spock.JazonSpockAdapter.jazon

@Ignore
class ExampleSpec extends Specification {

    def "smoke test"() {
        expect:
        jazon('{"wegorz": "elektryczny"}').matches([wegorz: 'elektryczny'])
    }

    def "failure format test"() {
        expect:
        jazon('{"wegorz": "elektryczny", "ryba": "pila"}').matches([wegorz: 'elektryczny'])
    }
}
