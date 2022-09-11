package com.zendesk.jazon.mismatch.impl;

import com.zendesk.jazon.mismatch.Mismatch;
import com.zendesk.jazon.mismatch.MismatchWithPathFactory;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@EqualsAndHashCode
@ToString
public class PredicateExecutionFailedMismatch implements Mismatch, MismatchWithPathFactory {
    private final Throwable cause;

    public PredicateExecutionFailedMismatch(Throwable cause) {
        this.cause = checkNotNull(cause);
    }

    @Override
    public String message() {
        StringWriter stringWriter = new StringWriter();

        stringWriter.append("Exception occurred on predicate evaluation: \n\n");
        cause.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }
}
