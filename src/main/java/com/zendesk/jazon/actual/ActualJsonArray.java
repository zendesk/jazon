package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.unmodifiableList;

@ToString
@EqualsAndHashCode
public class ActualJsonArray implements Actual {
    private final List<Actual> list;

    ActualJsonArray(List<Actual> list) {
        this.list = checkNotNull(list);
    }

    public List<Actual> list() {
        return unmodifiableList(list);
    }

    @Override
    public JazonMatchResult accept(JsonExpectation expectation) {
        return expectation.match(this);
    }
}
