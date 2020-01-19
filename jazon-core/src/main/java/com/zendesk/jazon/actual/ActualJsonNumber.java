package com.zendesk.jazon.actual;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

@EqualsAndHashCode
public class ActualJsonNumber implements Actual {
    private final Number number;

    public ActualJsonNumber(Number number) {
        checkPreconditions(number);
        this.number = maybeConvertLongToInt(number);
    }

    public Number number() {
        return number;
    }

    @Override
    public MatchResult accept(JsonExpectation expectation, String path) {
        return expectation.match(this, path);
    }

    @Override
    public String toString() {
        return number.toString();
    }

    private static void checkPreconditions(Number number) {
        checkNotNull(number);
        checkSupportedType(number);
    }

    private static void checkSupportedType(Number number) {
        if (isOfSupportedType(number)) {
            return;
        }
        throw new IllegalArgumentException(
                String.format(
                        "Given Number must be either Integer, Long, BigDecimal, Float or Double. Found: %s (%s)",
                        number,
                        number.getClass()
                )
        );
    }

    private static boolean isOfSupportedType(Number number) {
        return number instanceof Integer ||
                number instanceof Long ||
                number instanceof BigDecimal ||
                number instanceof Float ||
                number instanceof Double;
    }

    private Number maybeConvertLongToInt(Number number) {
        if (number instanceof Long
                && number.longValue() <= Integer.MAX_VALUE
                && number.longValue() >= Integer.MIN_VALUE) {
            return number.intValue();
        }
        return number;
    }
}
