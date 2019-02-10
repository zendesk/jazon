package com.zendesk.jazon.expectation;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.actual.*;

public interface JsonExpectation {
    JazonMatchResult match(ActualJsonNumber actualNumber);
    JazonMatchResult match(ActualJsonObject actualObject);
    JazonMatchResult match(ActualJsonString actualString);
    JazonMatchResult match(ActualJsonNull actualNull);
    JazonMatchResult match(ActualJsonArray actualArray);
    JazonMatchResult match(ActualJsonBoolean actualBoolean);
}
