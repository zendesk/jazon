package com.zendesk;

interface JsonExpectation {
    JazonMatchResult match(ActualJsonNumber actualNumber);
    JazonMatchResult match(ActualJsonObject actualObject);
    JazonMatchResult match(ActualJsonString actualString);
//    JazonMatchResult match(JsonNull object);
}
