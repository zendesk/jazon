package com.zendesk.jazon.expectation.translator;

import com.zendesk.jazon.expectation.JsonExpectation;

public interface Translator<T> {
    JsonExpectation jsonExpectation(T object, TranslatorFacade translator);
}
