package com.bep.lingo.game.domain.exception;

//https://github.com/sastani/Hangman/blob/master/src/main/java/com/exceptions/GameOverException.java
public class GameOverException extends Exception {
    public GameOverException(String id) { super(String.format("Game with id: %s is already complete. please input your name to register your score.", id)); }
}
