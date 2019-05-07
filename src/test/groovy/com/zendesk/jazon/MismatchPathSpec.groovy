package com.zendesk.jazon

import com.zendesk.jazon.actual.ActualFactory
import com.zendesk.jazon.actual.ObjectsActualFactory
import com.zendesk.jazon.expectation.DefaultExpectationFactory
import com.zendesk.jazon.expectation.ExpectationFactory
import com.zendesk.jazon.mismatch.PrimitiveValueMismatch
import spock.lang.Specification

class MismatchPathSpec extends Specification {

    ActualFactory actualFactory = new ObjectsActualFactory()
    ExpectationFactory expectationFactory = new DefaultExpectationFactory()
    MatcherFactory matcherFactory = new MatcherFactory(
            expectationFactory,
            actualFactory
    )

    def "complex case"() {
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
        result.mismatch().expectationMismatch() == primitiveValueMismatch(8, 99)
        result.mismatch().path() == '$.data.2.values.b'
    }

    def "complex case 2"() {
        given:
        def expected = VERY_COMPLEX_OBJECT
        def actual = VERY_COMPLEX_OBJECT_WITH_ONE_FIELD_DIFFERENT

        when:
        def result = match(expected, actual)

        then:
        !result.ok()
        result.mismatch().expectationMismatch() == primitiveValueMismatch("green", "black")
        result.mismatch().path() == '$.data.1.values.elements.0.value'
    }

    private MatchResult match(Map expected, Map actual) {
        matcherFactory.matcher()
                .expected(expected)
                .actual(actual)
                .match()
    }

    private PrimitiveValueMismatch primitiveValueMismatch(def expected, def actual) {
        return new PrimitiveValueMismatch(actualFactory.actual(expected), actualFactory.actual(actual))
    }

    private static final VERY_COMPLEX_OBJECT = [
            data: [
                    [
                            key   : 111,
                            values: [
                                    a       : 1,
                                    b       : "one",
                                    elements: [
                                            [
                                                    name: "color",
                                                    value: "blue"
                                            ],
                                            [
                                                    name: "width",
                                                    value: 5
                                            ],
                                            [
                                                    name: "height",
                                                    value: 10
                                            ],
                                    ]
                            ]
                    ],
                    [
                            key   : 111,
                            values: [
                                    a: 2,
                                    b: "two",
                                    elements: [
                                            [
                                                    name: "color",
                                                    value: "green"
                                            ],
                                            [
                                                    name: "width",
                                                    value: 12
                                            ],
                                            [
                                                    name: "height",
                                                    value: 24
                                            ],
                                    ]
                            ]
                    ],
                    [
                            key   : 111,
                            values: [
                                    a: 3,
                                    b: "three",
                                    elements: [
                                            [
                                                    name: "color",
                                                    value: "red"
                                            ],
                                            [
                                                    name: "width",
                                                    value: 33
                                            ],
                                            [
                                                    name: "height",
                                                    value: 66
                                            ],
                                    ]
                            ]
                    ],
                    [
                            key   : 111,
                            values: [
                                    a: 4,
                                    b: "four",
                                    elements: [
                                            [
                                                    name: "color",
                                                    value: "orange"
                                            ],
                                            [
                                                    name: "width",
                                                    value: 25
                                            ],
                                            [
                                                    name: "height",
                                                    value: 50
                                            ],
                                    ]
                            ]
                    ],
            ]
    ]

    private static final VERY_COMPLEX_OBJECT_WITH_ONE_FIELD_DIFFERENT = [
            data: [
                    [
                            key   : 111,
                            values: [
                                    a       : 1,
                                    b       : "one",
                                    elements: [
                                            [
                                                    name: "color",
                                                    value: "blue"
                                            ],
                                            [
                                                    name: "width",
                                                    value: 5
                                            ],
                                            [
                                                    name: "height",
                                                    value: 10
                                            ],
                                    ]
                            ]
                    ],
                    [
                            key   : 111,
                            values: [
                                    a: 2,
                                    b: "two",
                                    elements: [
                                            [
                                                    name: "color",
                                                    value: "black"
                                            ],
                                            [
                                                    name: "width",
                                                    value: 12
                                            ],
                                            [
                                                    name: "height",
                                                    value: 24
                                            ],
                                    ]
                            ]
                    ],
                    [
                            key   : 111,
                            values: [
                                    a: 3,
                                    b: "three",
                                    elements: [
                                            [
                                                    name: "color",
                                                    value: "red"
                                            ],
                                            [
                                                    name: "width",
                                                    value: 33
                                            ],
                                            [
                                                    name: "height",
                                                    value: 66
                                            ],
                                    ]
                            ]
                    ],
                    [
                            key   : 111,
                            values: [
                                    a: 4,
                                    b: "four",
                                    elements: [
                                            [
                                                    name: "color",
                                                    value: "orange"
                                            ],
                                            [
                                                    name: "width",
                                                    value: 25
                                            ],
                                            [
                                                    name: "height",
                                                    value: 50
                                            ],
                                    ]
                            ]
                    ],
            ]
    ]
}
