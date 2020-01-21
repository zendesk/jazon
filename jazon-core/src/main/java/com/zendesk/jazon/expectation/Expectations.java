package com.zendesk.jazon.expectation;

public class Expectations {
    
    public static AnyNumberOf anyNumberOf(Object element) {
        return new AnyNumberOf(element);
    }
}
