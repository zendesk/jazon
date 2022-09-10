package com.zendesk.jazon.expectation;

interface Translator<T> {
    JsonExpectation jsonExpectation(T object, TranslatorFacade translator);
}
