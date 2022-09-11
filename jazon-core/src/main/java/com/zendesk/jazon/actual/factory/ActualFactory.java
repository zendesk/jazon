package com.zendesk.jazon.actual.factory;

import com.zendesk.jazon.actual.Actual;

public interface ActualFactory<T> {
    Actual actual(T input);
}
