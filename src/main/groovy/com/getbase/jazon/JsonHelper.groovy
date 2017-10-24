package com.getbase.jazon

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Charsets
import com.google.common.io.Resources

class JsonHelper {

    private final static ObjectMapper MAPPER = ObjectMapper.newInstance()

    private final static ESCAPE = ['{', '}', '[', ']', '(', ')', '?']

    private final static REPLACE = ['>': '}', '<': '{', '\\\\': '\\', '@\\(': '(', '\\)@': ')']

    private final static REMOVE = ['"@', '@"']

    static {
        MAPPER.configure(com.fasterxml.jackson.databind.SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        MAPPER.configure(com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true) //this prevents scientific notation in re-serialized json
    }

    static String fixture(String fileName) {
        URL url = Resources.getResource(fileName)
        return Resources.toString(url, Charsets.UTF_8)
    }

    static String jsonFixture(String fileName) {
        URL url = Resources.getResource(fileName)
        String json = Resources.toString(url, Charsets.UTF_8)
        return normalized(json)
    }

    static String jsonRegexpFixture(String filename) {
        def json = jsonFixture(filename)
        jsonRegexp(json)
    }

    static jsonRegexp(String json) {
        ESCAPE.each { token -> json = json.replace(token, "\\$token") }
        REPLACE.each { k, v -> json = json.replace(k, v) }
        REMOVE.each { token -> json = json.replace(token, '') }
        json
    }

    /**
     * @return formatted json with keys sorted alphabetically
     */
    static String normalized(String json) {
        MAPPER.writeValueAsString(MAPPER.readValue(json, Object.class))
    }

    static Map jsonAsMap(String json) {
        MAPPER.readValue(json, Map.class)
    }

    static String mapAsJson(Map map) {
        MAPPER.writeValueAsString(map)
    }

//    static String jsonFixtureInterpolated(String fileName, Map values) {
//        URL url = Resources.getResource(fileName)
//        String json = Resources.toString(url, Charsets.UTF_8)
//        def substitutor = new StrSubstitutor(values, '$(', ')')
//        return normalized(substitutor.replace(json))
//    }
}