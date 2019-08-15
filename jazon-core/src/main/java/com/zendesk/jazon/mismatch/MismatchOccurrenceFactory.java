package com.zendesk.jazon.mismatch;

import static java.util.Optional.empty;

interface MismatchOccurrenceFactory {
    default MismatchOccurrence at(String path) {
        return new MismatchOccurrence((Mismatch) this, path, empty());
    }
}
