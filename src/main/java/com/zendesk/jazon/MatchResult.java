package com.zendesk.jazon;

import com.zendesk.jazon.mismatch.JsonMismatch;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class MatchResult {
    private final Optional<JsonMismatch> mismatch;

    public static MatchResult success() {
        return new MatchResult(empty());
    }

    public static MatchResult failure(JsonMismatch mismatch) {
        return new MatchResult(of(mismatch));
    }

    private MatchResult(Optional<JsonMismatch> mismatch) {
        this.mismatch = mismatch;
    }

    public boolean ok() {
        return !mismatch.isPresent();
    }

    public String message() {
        return "JSON MISMATCH:\n" + mismatch.map(JsonMismatch::message).orElse("lol no mismatch!");
    }

    public JsonMismatch mismatch() {
        return mismatch.orElseThrow(IllegalArgumentException::new);
    }
}
