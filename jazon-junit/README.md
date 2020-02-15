# Jazon JUnit 
A library for test assertions on JSON payloads - for JUnit framework. 

## Quickstart

#### Example 1: Simple exact-match

For such JSON:
```json
{
  "firstname": "Steve",
  "lastname": "Jobs"
}
```

... an exact-match assertion would look like this:

```java
@Test
public void simpleTest() {
    // when
    String actualJson = getSteveJobsJson();

    // then
    assertThat(response).matches(
        new JazonMap()
            .with("firstname", "Steve")
            .with("lastname", "Jobs")
    );
}
```

#### Example 2: Unordered array

This assertion passes even though the items in the array are in different order.

The `actualJson`:
```json
{
    "id": 95478,
    "name": "Coca Cola",
    "tags": ["sprite", "pepsi", "7up", "fanta", "dr pepper"]
}
```

The assertion:
```java
assertThat(response).matches(
    new JazonMap()
        .with("id", 95478)
        .with("name", "Coca Cola")
        .with("tags", set("pepsi", "dr pepper", "sprite", "fanta", "7up"))
);
```
```java
private Set<Object> set(Object... elements) {
    HashSet<Object> result = new HashSet<>(elements.length);
    result.addAll(asList(elements));
    return result;
}
```

#### Example 3: Custom assertions

If you need, instead of exact-matching, you can define custom assertions using Predicates.
Here for example, we used custom assertions:
 * to check that a number is in given range - `(Integer id) -> id >= 0`
 * to check that a field matches a regex - `regex()`
 * to check that a field just exists, no matter of its value - `Objects::nonNull`

The `actualJson`:
```json
{
    "id": 95478,
    "name": "Coca Cola",
    "value": "133.30",
    "updated_at": "1990-06-19T12:19:10Z"
}
```

The assertion:
```java
assertThat(response).matches(
    new JazonMap()
        .with("id", (Integer id) -> id >= 0)
        .with("name", "Coca Cola")
        .with("value", regex("\\d+\\.\\d\\d"))
        .with("updated_at", Objects::nonNull)
);
```

```java
private Predicate<String> regex(String regex) {
    return value -> value.matches(regex);
}
```

#### Example 4: Utils extraction

To avoid code duplication, you can extract your common wildcard-assertions to constants.

```java
assertThat(response).matches(
    new JazonMap()
        .with("id", ANY_ID)         // a constant
        .with("name", "Coca Cola")
        .with("value", "133.30")
        .with("updated_at", ANY_ISO_DATETIME)   // a constant
);
```

#### Example 5: Utils extraction to domain objects

To avoid code duplication even more, you can extract the parts of JSON. This will also 
make your tests more readable.
Here in the examples we extract `deal()` - a business object from sales domain. 

```java
assertThat(response).matches(deal("Coca Cola", "10.00"));
```
```java
assertThat(response).matches(
    asList(
        deal("Coca Cola", "10.00"),
        deal("Pepsi", "9.00"),
        deal("Fanta", "10.00"),
        deal("Sprite", "10.00"),
        deal("Dr Pepper", "12.00")
    )
);
```
```java
private JazonMap deal(String name, String value) {
    return new JazonMap()
        .with("id", ANY_ID)
        .with("name", name)
        .with("value", value)
        .with("updated_at", ANY_ISO_DATETIME);
}
```

#### Examples code
You can check out the example tests [in the code](/examples/src/test/java/com/zendesk/jazon/junit/ReadmeExamplesTest.java) 

## Copyright and license
Copyright 2019 Zendesk, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

