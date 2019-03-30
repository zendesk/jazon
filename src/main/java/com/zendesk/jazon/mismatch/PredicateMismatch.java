package com.zendesk.jazon.mismatch;

import lombok.ToString;

@ToString
public enum PredicateMismatch implements JsonMismatch, LocJsonMismatchFactory {
    INSTANCE;
}
