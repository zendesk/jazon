package com.zendesk;

interface JsonExpectation {
    JazonMatchResult match(ActualJsonNumber actualNumber);
    JazonMatchResult match(ActualJsonObject actualObject);
//    JazonMatchResult match(JsonString object);
//    JazonMatchResult match(JsonNull object);
}
