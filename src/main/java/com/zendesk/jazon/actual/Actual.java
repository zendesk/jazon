package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

public interface Actual {
    JazonMatchResult accept(JsonExpectation expectation);
}
