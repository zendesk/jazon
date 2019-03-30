package com.zendesk.jazon.mismatch;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class JsonMismatchWithPath implements JsonMismatch {
    private final String path;

    public JsonMismatchWithPath(String path) {
        this.path = checkNotNull(path);
    }

    public String path() {
        return path;
    }
}
