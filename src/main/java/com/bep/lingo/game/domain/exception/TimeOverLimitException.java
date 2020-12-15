package com.bep.lingo.game.domain.exception;

public class TimeOverLimitException extends Exception {
    public TimeOverLimitException() {
        super("You took too long to guess! You must make a guess within 10 seconds!");
    }
}
