package com.zendesk.jazon.mismatch;

interface LocJsonMismatchFactory {
    default LocJsonMismatch at(String path) {
        return new LocJsonMismatch((JsonMismatch) this, path);
    }
}
