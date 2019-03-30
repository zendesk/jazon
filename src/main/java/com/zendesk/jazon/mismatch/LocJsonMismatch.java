package com.zendesk.jazon.mismatch;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocJsonMismatch implements JsonMismatch {
    private final JsonMismatch jsonMismatch;

    public LocJsonMismatch(JsonMismatch jsonMismatch) {
        this.jsonMismatch = checkNotNull(jsonMismatch);
    }

    public String path() {
        return ".";
    }
}
