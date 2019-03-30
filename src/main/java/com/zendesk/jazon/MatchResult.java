package com.zendesk.jazon;

import com.zendesk.jazon.mismatch.Mismatch;
import com.zendesk.jazon.mismatch.MismatchWithPath;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class MatchResult {
    private final Optional<MismatchWithPath> mismatch;

    public static MatchResult success() {
        return new MatchResult(empty());
    }

    public static MatchResult failure(MismatchWithPath mismatch) {
        return new MatchResult(of(mismatch));
    }

    private MatchResult(Optional<MismatchWithPath> mismatch) {
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
                .map(MismatchWithPath::expectationMismatch)
                .map(Mismatch::message)
                .orElse("lol no mismatch!");
    }

    public MismatchWithPath mismatch() {
        return mismatch.orElseThrow(IllegalArgumentException::new);
    }
}
