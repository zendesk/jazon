package com.zendesk.jazon.mismatch;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class LocJsonMismatch {
    private final JsonMismatch internalMismatch;
    private final String path;

    public LocJsonMismatch(JsonMismatch internalMismatch, String path) {
        this.internalMismatch = checkNotNull(internalMismatch);
        this.path = checkNotNull(path);
    }

    public JsonMismatch internalMismatch() {
        return internalMismatch;
    }

    public String path() {
        return path;
    }
}
