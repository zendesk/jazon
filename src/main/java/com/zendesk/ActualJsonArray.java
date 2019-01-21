package com.zendesk;

import java.util.List;

import static java.util.Collections.unmodifiableList;

class ActualJsonArray implements Actual {
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
