package com.bep.lingo.game.domain.exception;

public final class InvalidWordException extends Exception {
    public InvalidWordException(String w) {
        super(String.format("Guessed word %s is invalid.", w));
    }
}
