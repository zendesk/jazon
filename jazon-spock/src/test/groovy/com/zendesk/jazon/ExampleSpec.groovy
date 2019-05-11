package com.zendesk.jazon

import spock.lang.FailsWith
import spock.lang.Specification

import static com.zendesk.jazon.spock.JazonSpockAdapter.jazon

class ExampleSpec extends Specification {

    def "smoke test"() {
        expect:
        jazon('{"shark": "white"}').matches([shark: 'white'])
    }

    @FailsWith(AssertionError)
    def "failure format test"() {
        expect:
        jazon('{"shark": "white", "raccoon": "red"}').matches([shark: 'white'])
    }

    def "predicate expectation test"() {
        expect:
        jazon('{"shark": "white"}').matches([
                shark: { it.startsWith('whi') }
        ])
    }

    def "array can be root JSON: success"() {
        expect:
        jazon('["platypus", "narwhal"]').matches(['platypus', 'narwhal'])
    }

    @FailsWith(AssertionError)
    def "array can be root JSON: fails"() {
        expect:
        jazon('["platypus", "narwhal"]').matches(['platypus', 'lynx'])
    }
}
