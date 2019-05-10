package com.zendesk.jazon.actual;

public interface ActualFactory<T> {
    Actual actual(T input);
}
