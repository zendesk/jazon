package com.zendesk.jazon.mismatch;

public interface JsonMismatch {
    default String message() {
        return "ale beka: " + this.toString();
    }
}
