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
    assertThat(actualJson).matches(
            ImmutableMap.<String, Object>builder()
                    .put("firstname", "Steve")
                    .put("lastname", "Jobs")
                    .build()
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
assertThat(actualJson).matches(
        ImmutableMap.<String, Object>builder()
                .put("id", 95478)
                .put("name", "Coca Cola")
                .put("tags", newHashSet("pepsi", "dr pepper", "sprite", "fanta", "7up"))
                .build()
);
```

#### Example 3: Custom assertions

If you need, instead of exact-matching, you can define custom assertions using Predicates.
Here for example, we used custom assertions:
 * to check that a number is in given range - `anyId()`
 * to check that a field matches a regex - `regex()`
 * to check that a field just exists, no matter of its value - `notNull()`

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
assertThat(actualJson).matches(
        ImmutableMap.<String, Object>builder()
                .put("id", anyId())
                .put("name", "Coca Cola")
                .put("value", regex("\\d+\\.\\d\\d"))
                .put("updated_at", notNull())
                .build()
);
```

```java
private Predicate<Integer> anyId() {
    return value -> value >= 0;
}

private Predicate<Integer> notNull() {
    return value -> value != null;
}

private Predicate<String> regex(String regex) {
    return value -> value.matches(regex);
}
```

## Copyright and license
Copyright 2019 Zendesk, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

