package com.zendesk;

import static com.zendesk.JazonMatchResult.*;

class PrimitiveValueExpectation implements JsonExpectationBeka {
    private final Number expectedNumber;

    PrimitiveValueExpectation(Number expectedNumber) {
        this.expectedNumber = expectedNumber;
    }

    @Override
    public JazonMatchResult match(ActualJsonNumber actualNumber) {
        if (expectedNumber.equals(actualNumber.number())) {
            return success();
        }
        return failure(new NumberMismatch(expectedNumber, actualNumber.number()));
    }

    @Override
    public JazonMatchResult match(ActualJsonObject actualObject) {
        return null;
    }
}
