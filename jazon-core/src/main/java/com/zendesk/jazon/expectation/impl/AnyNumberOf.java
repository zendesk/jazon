package com.zendesk.jazon.expectation.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnyNumberOf {
    @Getter
    private final Object elementExpectation;
}
