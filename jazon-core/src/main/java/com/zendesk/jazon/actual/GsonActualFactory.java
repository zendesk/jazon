package com.zendesk.jazon.actual;

import com.google.gson.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class GsonActualFactory implements ActualFactory<String> {
    private static final JsonParser JSON_PARSER = new JsonParser();

    @Override
    public Actual actual(String input) {
        return fromJsonElement(JSON_PARSER.parse(input));
    }

    private Actual fromJsonElement(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            return actualJsonObject((JsonObject) jsonElement);
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = (JsonArray) jsonElement;
            return actualJsonArray(jsonArray);
        } else if (jsonElement.isJsonNull()) {
            return ActualJsonNull.INSTANCE;
        } else if (jsonElement.isJsonPrimitive()) {
            return actualJsonPrimitive((JsonPrimitive) jsonElement);
        }
        throw new IllegalStateException("Invalid JsonElement - not Object, not Array, not Null, not Primitive");
    }

    private ActualJsonObject actualJsonObject(JsonObject jsonObject) {
        Map<String, Actual> map = jsonObject.entrySet()
                .stream()
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                e -> this.fromJsonElement(e.getValue()),
                                (a, b) -> a,
                                LinkedHashMap::new
                        )
                );
        return new ActualJsonObject(map);
    }

    private ActualJsonArray actualJsonArray(JsonArray jsonArray) {
        List<Actual> elements = stream(jsonArray)
                .map(this::fromJsonElement)
                .collect(toList());
        return new ActualJsonArray(elements);

    }

    private Actual actualJsonPrimitive(JsonPrimitive jsonPrimitive) {
        if (jsonPrimitive.isBoolean()) {
            return new ActualJsonBoolean(jsonPrimitive.getAsBoolean());
        } else if (jsonPrimitive.isNumber()) {
            return new ActualJsonNumber(intOrLongOrBigDecimal(jsonPrimitive));
        } else if (jsonPrimitive.isString()) {
            return new ActualJsonString(jsonPrimitive.getAsString());
        }
        throw new IllegalStateException("Invalid JsonPrimitive - not Boolean, not Number, not String");
    }

    private Number intOrLongOrBigDecimal(JsonPrimitive jsonPrimitive) {
        String numberAsString = jsonPrimitive.getAsString();
        if (isInteger(numberAsString)) {
            long numberAsLong = Long.parseLong(numberAsString);
            if (numberAsLong <= Integer.MAX_VALUE && numberAsLong >= Integer.MIN_VALUE) {
                return (int) numberAsLong;
            }
            return numberAsLong;
        }
        return new BigDecimal(numberAsString);
    }

    private boolean isInteger(String numberAsString) {
        return !numberAsString.contains(".") &&
                !numberAsString.contains("e") &&
                !numberAsString.contains("E");
    }

    private Stream<JsonElement> stream(JsonArray jsonArray) {
        return StreamSupport.stream(jsonArray.spliterator(), false);
    }
}
