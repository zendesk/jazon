package com.zendesk.jazon.expectation.translator;

import com.zendesk.jazon.expectation.JsonExpectation;

interface Translator<T> {
    JsonExpectation jsonExpectation(T object, TranslatorFacade translator);
}
