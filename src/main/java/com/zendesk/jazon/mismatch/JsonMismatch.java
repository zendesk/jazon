package com.zendesk.jazon.mismatch;

public interface JsonMismatch {
//    String path();
    default String message() {
        return "ale beka: " + this.toString();
    }
}
