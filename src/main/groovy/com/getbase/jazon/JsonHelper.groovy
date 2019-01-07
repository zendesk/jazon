package com.getbase.jazon


import com.google.common.base.Charsets
import com.google.common.io.Resources
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class JsonHelper {

    private final static ESCAPE = ['{', '}', '[', ']', '(', ')', '?']

    private final static REPLACE = ['>': '}', '<': '{', '\\\\': '\\', '@\\(': '(', '\\)@': ')']

    private final static REMOVE = ['"@', '@"']

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
        JsonOutput.toJson(json)
    }

    static Map jsonAsMap(String json) {
        new JsonSlurper().parseText(json) as Map
    }

    static String mapAsJson(Map map) {
        JsonOutput.toJson(map)
    }

//    static String jsonFixtureInterpolated(String fileName, Map values) {
//        URL url = Resources.getResource(fileName)
//        String json = Resources.toString(url, Charsets.UTF_8)
//        def substitutor = new StrSubstitutor(values, '$(', ')')
//        return normalized(substitutor.replace(json))
//    }
}