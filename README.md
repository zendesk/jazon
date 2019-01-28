# jazon
Test assertions on JSONs has never been easier

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
    
