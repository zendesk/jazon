package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class ActualJsonArray implements Actual {
    private final List<Actual> list;

    ActualJsonArray(List<Actual> list) {
        this.list = list;
    }

    public List<Actual> list() {
        return unmodifiableList(list);
    }

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }
}
