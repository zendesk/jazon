package com.zendesk.jazon.actual;

import com.zendesk.jazon.JazonMatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "ActualJsonArray{" +
                "list=" + list +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActualJsonArray that = (ActualJsonArray) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
