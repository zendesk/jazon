package com.getbase.jazon

class NotMatchedException extends RuntimeException {
    public final MatchResult matchResult;

    def NotMatchedException(MatchResult matchResult) {
        this.matchResult = matchResult;
    }


}
