package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;

public interface JsonExpectation {
    MatchResult match(ActualJsonNumber actualNumber);
    MatchResult match(ActualJsonObject actualObject);
    MatchResult match(ActualJsonString actualString);
    MatchResult match(ActualJsonNull actualNull);
    MatchResult match(ActualJsonArray actualArray);
    MatchResult match(ActualJsonBoolean actualBoolean);
}
