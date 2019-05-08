# jazon
Test assertions on JSONs have never been easier

## Features
Jazon supports:
* simple assertions on JSONs
    * Groovy example: 
    ```groovy
    jazon(response).matches(
        [
            firstName: 'Steve', 
            lastName: 'Jobs'
        ]
    )
    ```
* assertions for unordered arrays (when order of items do not matter in your JSON response)
    * This Groovy example will pass for `[1, 2, 3]`, `[3, 1, 2]`, `[2, 3, 1]` as response's `"elements"` field: 
    ```groovy
    jazon(response).matches(
        [
            elements: [1, 2, 3] as Set
        ]
    )
    ```
* handy user-defined assertions (most powerful productivity booster)
    * Groovy example:
    ```groovy
    jazon(response).matches(
        [
            id: ANYTHING,
            elements: { it.size() == 17 }  
        ]
    )
    ```
* optimised to minimise code duplication
    * Groovy example:
    ```groovy
    jazon(response).matches person(lastName: 'Jobs')
    ```
    ```groovy
    def person(Map kwargs) {
      Map defaults = [
          id: ANYTHING,
          firstName: ANY_STRING_OR_NULL,
          lastName: ANY_STRING,
      ]
      return defaults + kwargs
    }
    ```     
* optimised for best human-readable error message allowing for fast error tracing
    * Sample error message: `Mismatch at path '$.data.deal.name' - Expected: "Big Deal" but found: null`. 
    
## Copyright and license
Copyright 2019 Zendesk, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

