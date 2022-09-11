package com.zendesk.jazon


import com.zendesk.jazon.actual.factory.GsonActualFactory
import com.zendesk.jazon.expectation.translator.DefaultTranslators
import com.zendesk.jazon.expectation.translator.JazonTypesTranslators
import com.zendesk.jazon.expectation.translator.SpockTranslators
import com.zendesk.jazon.expectation.translator.TranslatorFacade
import spock.lang.Specification

import static groovy.json.JsonOutput.toJson

class MessagesForGroovySpec extends Specification {

    MatcherFactory matcherFactory = new MatcherFactory(
            new TranslatorFacade(
                    DefaultTranslators.translators() + JazonTypesTranslators.translators() + SpockTranslators.translators()
            ),
            new GsonActualFactory()
    )

    def "predicate expectation: fails"() {
        given:
        Closure closure = { it ==~ "dig.*" }

        when:
        def result = matcherFactory.matcher()
                .expected([a: closure])
                .actual(toJson([a: 'rosemary']))
                .match()

        then:
        result.message() == 'Mismatch at path: $.a\nCustom predicate does not match the value.'
        println result.message()
    }
}
