package com.zendesk.jazon.expectation;

import com.zendesk.jazon.expectation.impl.AnyNumberOf;

public class Expectations {
    
    public static AnyNumberOf anyNumberOf(Object element) {
        return new AnyNumberOf(element);
    }
}
