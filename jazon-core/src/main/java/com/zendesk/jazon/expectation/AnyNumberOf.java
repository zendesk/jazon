package com.zendesk.jazon.expectation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnyNumberOf {
    @Getter
    private final Object elementExpectation;
}
