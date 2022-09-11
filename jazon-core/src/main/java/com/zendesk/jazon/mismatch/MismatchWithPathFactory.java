package com.zendesk.jazon.mismatch;

public interface MismatchWithPathFactory {
    default MismatchWithPath at(String path) {
        return new MismatchWithPath((Mismatch) this, path);
    }
}
