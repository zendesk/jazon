# Jazon Spock 
A library for test assertions on JSON payloads - for Spock framework. 

## Quickstart

#### Example 1: Simple exact-match

```groovy
def "simple assertion passes"() {
    when:
    def response = '''
        {
            "firstname": "Steve",
            "lastname": "Jobs"
        }
    '''

    then:
    jazon(response).matches([firstname: 'Steve', lastname: 'Jobs'])
}
```

#### Example 2: Unordered array

This assertion passes even though the items in the array are in different order. 

```groovy
def "unordered array assertion passes"() {
    when:
    def response = '''
        {
            "id": 95478,
            "name": "Coca Cola",
            "tags": ["sprite", "pepsi", "7up", "fanta", "dr pepper"]
        }
    '''

    then:
    jazon(response).matches([
            id: 95478,
            name: 'Coca Cola',
            tags: ['pepsi', 'dr pepper', 'sprite', 'fanta', '7up'] as Set
    ])
}
```

#### Example 3: Custom assertions

If you need, instead of exact-matching, you can define custom assertions using Closures.
Here for example, we used custom assertions:
 * to check that a number is in given range - `{ it >= 0 }`
 * to check that field matches a regex - `{ it ==~ /\d+\.\d\d/ }`
 * to check that field just exists, no matter of its value - `{ it != null }`

```groovy
def "custom assertions"() {
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
            value: { it ==~ /\d+\.\d\d/ },
            updated_at: { it != null }
    ])
}
```

#### Example 4: Utils extraction

To avoid code duplication, you can extract you common wildcard-assertions to constants. 

```groovy
def "utils extraction"() {
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
            id: ANY_ID,         // a constant
            name: 'Coca Cola',
            value: '133.30',
            updated_at: ANY_ISO_DATETIME    // a constant
    ])
}
```

#### Example 5: Even more utils extraction

To avoid code duplication even more, you can extract the parts of JSON. This will also 
make your tests more readable.
Here in the examples we extract `deal()` - a business object from sales domain. 

```groovy
jazon(response).matches deal(name: 'Coca Cola', value: '10.00')
```

```groovy
jazon(response).matches([
    deal(name: 'Coca Cola', value: '10.00'),
    deal(name: 'Pepsi', value: '9.00'),
    deal(name: 'Fanta', value: '10.00'),
    deal(name: 'Sprite', value: '10.00'),
    deal(name: 'Dr Pepper', value: '12.00')
])
```

```groovy
private Map deal(Map kwargs) {
    Map defaults = [
            id: ANY_ID,
            updated_at: ANY_ISO_DATETIME
    ]
    defaults + kwargs
}
```

## Copyright and license
Copyright 2019 Zendesk, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

