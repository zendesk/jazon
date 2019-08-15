package com.zendesk.jazon.mismatch;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class MismatchOccurrence {
    private final Mismatch internalMismatch;
    private final String path;
    private final Optional<Throwable> cause;

    MismatchOccurrence(Mismatch internalMismatch, String path, Optional<Throwable> cause) {
        this.internalMismatch = checkNotNull(internalMismatch);
        this.path = checkNotNull(path);
        this.cause = checkNotNull(cause);
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

    public Optional<Throwable> cause() {
        return cause;
    }

    public MismatchOccurrence causedBy(Throwable cause) {
        return new MismatchOccurrence(internalMismatch, path, Optional.of(cause));
    }
}
