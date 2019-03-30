package com.zendesk.jazon;

import com.zendesk.jazon.mismatch.JsonMismatch;
import com.zendesk.jazon.mismatch.LocJsonMismatch;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class MatchResult {
    private final Optional<LocJsonMismatch> mismatch;

    public static MatchResult success() {
        return new MatchResult(empty());
    }

    public static MatchResult failure(LocJsonMismatch mismatch) {
        return new MatchResult(of(mismatch));
    }

    private MatchResult(Optional<LocJsonMismatch> mismatch) {
        this.mismatch = mismatch;
    }

    public boolean ok() {
        return !mismatch.isPresent();
    }

    public String message() {
        return "JSON MISMATCH:\n" + mismatchMessage();
    }

    private String mismatchMessage() {
        return mismatch
                .map(LocJsonMismatch::internalMismatch)
                .map(JsonMismatch::message)
                .orElse("lol no mismatch!");
    }

    public LocJsonMismatch mismatch() {
        return mismatch.orElseThrow(IllegalArgumentException::new);
    }
}
