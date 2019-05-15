package com.zendesk.jazon.actual;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;
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
    public MatchResult accept(JsonExpectation expectation, String path) {
        return expectation.match(this, path);
    }
}
