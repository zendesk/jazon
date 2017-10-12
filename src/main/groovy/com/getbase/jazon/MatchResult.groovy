package com.getbase.jazon

class MatchResult {
    static MatchResult SUCCESS = new MatchResult(Result.SUCCESS, "");

    static enum Result {SUCCESS, FAILURE}

    public Result result;
    public String context;

    MatchResult(Result result, String context) {
        this.result = result
        this.context = context
    }
}
