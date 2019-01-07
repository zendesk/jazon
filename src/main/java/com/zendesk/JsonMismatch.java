package com.zendesk;

public interface JsonMismatch {
//    String path();
    default String message() {
        return "ale beka: " + this.toString();
    }
}
