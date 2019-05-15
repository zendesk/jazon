# Jazon
A library for test assertions on JSON outputs. 

Supports Spock and JUnit. Easy to extend for other test frameworks and languages.

## About

Jazon was created to make writing tests on JSON APIs easy. It offers:
* Simple exact-match assertions on JSON
* Matching unordered JSON arrays (ability to ignore the items order)
* User-defined wildcard assertions, e.g.
    * Match string to a regex
    * Match number to a range
    * Match datetime to some specific format
    * Verify that float has only 2 decimal points
    * Verify that list has only 25 items
    * ... anything you need
* Human-readable error messages for fast mismatch tracing
* Optimised to minimise code duplication 

## Using Jazon in you project

Jazon is provided as separate libraries (so called adapters) for each supported testing framework.
Depending on the framework you use, pick the adapter library that is applicable for you.

### Spock

[User guide for Spock Adapter](jazon-spock/README.md#Quickstart)

##### Gradle: 
```groovy
dependencies {
    testCompile 'com.zendesk.jazon:jazon-spock:0.3.0'
}
```
##### Maven:
```xml
<dependency>
    <groupId>com.zendesk.jazon</groupId>
    <artifactId>jazon-spock</artifactId>
    <version>0.3.0</version>
    <scope>test</scope>
</dependency>
```
 
### JUnit:

[User guide for JUnit Adapter](jazon-junit/README.md#Quickstart)

##### Gradle: 
```groovy
dependencies {
    testCompile 'com.zendesk.jazon:jazon-junit:0.3.0'
}
```
##### Maven:
```xml
<dependency>
    <groupId>com.zendesk.jazon</groupId>
    <artifactId>jazon-junit</artifactId>
    <version>0.3.0</version>
    <scope>test</scope>
</dependency>
```

## Copyright and license
Copyright 2019 Zendesk, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

