package com.zendesk.jazon;

import com.zendesk.jazon.mismatch.MismatchWithPath;

import java.util.Optional;
import java.util.function.Supplier;

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
        return mismatch
                .map(MismatchWithPath::message)
                .orElseThrow(cannotGetMessageException());
    }

    public MismatchWithPath mismatch() {
        return mismatch
                .orElseThrow(cannotGetMismatchException());
    }

    private Supplier<IllegalStateException> cannotGetMessageException() {
        return () -> new IllegalStateException("MatchResult is OK. There is no Mismatch. You cannot get the message.");
    }

    private Supplier<IllegalStateException> cannotGetMismatchException() {
        return () -> new IllegalStateException("MatchResult is OK. There is no Mismatch.");
    }
}
