package com.zendesk.jazon.mismatch;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class MismatchWithPath {
    private final Mismatch internalMismatch;
    private final String path;

    public MismatchWithPath(Mismatch internalMismatch, String path) {
        this.internalMismatch = checkNotNull(internalMismatch);
        this.path = checkNotNull(path);
    }

    public Mismatch expectationMismatch() {
        return internalMismatch;
    }

    public String path() {
        return path;
    }

    public String message() {
        return "Mismatch at path: " + path + "\n" + internalMismatch.message();
    }
}
