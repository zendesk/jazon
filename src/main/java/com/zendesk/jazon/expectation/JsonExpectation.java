package com.zendesk.jazon.expectation;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.actual.*;

public interface JsonExpectation {
    MatchResult match(ActualJsonNumber actualNumber, String path);
    MatchResult match(ActualJsonObject actualObject, String path);
    MatchResult match(ActualJsonString actualString, String path);
    MatchResult match(ActualJsonNull actualNull, String path);
    MatchResult match(ActualJsonArray actualArray, String path);
    MatchResult match(ActualJsonBoolean actualBoolean, String path);
}
