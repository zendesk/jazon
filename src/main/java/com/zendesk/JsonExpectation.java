package com.zendesk;

interface JsonExpectation {
    JazonMatchResult match(ActualJsonNumber actualNumber);
    JazonMatchResult match(ActualJsonObject actualObject);
    JazonMatchResult match(ActualJsonString actualString);
    JazonMatchResult match(ActualJsonNull actualNull);
    JazonMatchResult match(ActualJsonArray actualArray);
}
