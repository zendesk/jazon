package com.zendesk.jazon.actual;

import com.zendesk.jazon.MatchResult;
import com.zendesk.jazon.expectation.JsonExpectation;

import java.math.BigDecimal;
import java.util.Objects;

import static com.zendesk.jazon.util.Preconditions.checkNotNull;

public class ActualJsonNumber implements Actual {
    private final Number number;

    public ActualJsonNumber(Number number) {
        this.number = checkPreconditions(number);
    }

    public Number number() {
        return number;
    }

    @Override
    public MatchResult accept(JsonExpectation expectation, String path) {
        return expectation.match(this, path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActualJsonNumber that = (ActualJsonNumber) o;
        if (number.longValue() != that.number.longValue()) {
            return false;
        } else if (number.doubleValue() != that.number.doubleValue()) {
            return false;
        } else if (number instanceof BigDecimal || that.number instanceof BigDecimal) {
            if (number instanceof BigDecimal && that.number instanceof BigDecimal) {
                return number.equals(that.number);
            } else {
                /*
                Intentionally eagerly not matching of BigDecimal to something that is not BigDecimal.
                Why? This is mostly because hashCode() differs for that case.
                Why also? Also it is sensible for case:
                    * The other number is of Integer/Long type. Then you could expect that "1.0" and "1" do not match.
                    * The other number is of Float/Double type. Then, provided that there was a mismatch
                        on comparing `doubleValue()`, it is 100% sure that the BigDecimal stores a fraction that needs
                        so much precision so that it cannot be represented as a Float/Double.
                 */
                return false;
            }
        } else if (number instanceof Float || that.number instanceof Float) {
            if (number instanceof Integer || number instanceof Long
                    || that.number instanceof Integer || that.number instanceof Long) {
                /*
                Intentionally eagerly not matching of Float to something that is Integer or Long.
                Why? This is mostly because hashCode() differs for that case.
                Why also? Also it is sensible to expect that "1.0" and "1" do not match.
                 */
                //
                return false;
            }
        } else if (number instanceof Double || that.number instanceof Double) {
            if (number instanceof Integer || number instanceof Long
                    || that.number instanceof Integer || that.number instanceof Long) {
                /*
                Intentionally eagerly not matching of Double to something that is Integer or Long.
                Why? This is mostly because hashCode() differs for that case.
                Why also? Also it is sensible to expect that "1.0" and "1" do not match.
                 */
                return false;
            }
        } else if (number.intValue() == that.number.intValue()) {
            return true;
        }
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return number.toString();
    }

    private static Number checkPreconditions(Number number) {
        checkNotNull(number);
        checkSupportedType(number);
        return number;
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
}
