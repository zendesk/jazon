package com.zendesk.jazon

import spock.lang.Specification

import static com.zendesk.jazon.spock.JazonSpockAdapter.jazon

class EngineeringSummitShowcaseSpec extends Specification {

    def "simple case"() {
        when:
        def response = '''
            {
                "firstname": "Steve",
                "lastname": "Jobs",
            }
        '''

        then:
        jazon(response).matches([firstname: 'Steve', lastname: 'Jobs'])
    }










    def "deeply nested failure"() {
        when:
        def response = '''
            {
                "meta": {
                    "type": "collection",
                    "count": 2,
                    "links": {
                      "self": "http://api.getbase.com/v2/deals"
                    }
                },
                "items": [
                    {
                        "data": {
                            "id": 65478,
                            "name": "Big Deal",
                            "tags": ["serious", "big", "money"]
                        }
                    },
                    {
                        "data": {
                            "id": 77865,
                            "name": "Coca Cola",
                            "tags": ["pepsi", "sprite"]
                        }
                    }
                ],
            }
        '''

        then:
        jazon(response).matches([
                items: [
                        [
                                data: [
                                        id: 65478,
                                        name: 'Big Deal',
                                        tags: ['serious', 'big']
                                ]
                        ],
                        [
                                data: [
                                        id: 77865,
                                        name: 'Coca Cola',
                                        tags: ['pepsi', 'sprite']
                                ]
                        ],
                ],
                meta: [
                        type: 'collection',
                        count: 2,
                        links: 'http://api.getbase.com/v2/deals'
                ]
        ])
    }










    def "unordered array assertion"() {
        when:
        def response = '''
            {
                "id": 95478,
                "name": "Coca Cola",
                "tags": ["sprite", "pepsi", "7up", "fanta"]
            }
        '''

        then:
        jazon(response).matches([
                id: 95478,
                name: 'Coca Cola',
                tags: ['pepsi', 'dr pepper', 'sprite', 'fanta', '7up'] as Set
        ])
    }










    def "wildcards"() {
        when:
        def response = '''
            {
                "id": 95478,
                "name": "Coca Cola",
                "value": "133.30",
                "updated_at": "1990-06-19T12:19:10Z"
            }
        '''

        then:
        jazon(response).matches([
                id: { it >= 0 },
                name: 'Coca Cola',
                value: '133.30',
                updated_at: { it != null }
        ])
    }










    def "against code duplication"() {
        when:
        def response = '''
            {
                "id": 95478,
                "name": "Coca Cola",
                "value": "133.30",
                "updated_at": "1990-06-19T12:19:10Z"
            }
        '''

        then:
        jazon(response).matches deal(value: '133.30', name: 'Coca Cola')
    }

    private Map deal(Map kwargs) {
        def defaults = [
                id        : { it >= 0 },
                updated_at: { it != null }
        ]
        defaults + kwargs
    }


// /\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z/
}
