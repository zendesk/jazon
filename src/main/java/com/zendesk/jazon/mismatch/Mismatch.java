package com.zendesk.jazon.mismatch;

public interface Mismatch {
    default String message() {
        return "ale beka: " + this.toString();
    }
}
