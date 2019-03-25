package com.zendesk.jazon.actual;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

public interface Actual {
    MatchResult accept(JsonExpectation expectation);
}
