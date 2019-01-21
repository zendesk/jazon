package com.zendesk.jazon;

import com.zendesk.jazon.mismatch.JsonMismatch;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class JazonMatchResult {
    private final Optional<JsonMismatch> mismatch;

    public static JazonMatchResult success() {
        return new JazonMatchResult(empty());
    }

    public static JazonMatchResult failure(JsonMismatch mismatch) {
        return new JazonMatchResult(of(mismatch));
    }

    private JazonMatchResult(Optional<JsonMismatch> mismatch) {
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
