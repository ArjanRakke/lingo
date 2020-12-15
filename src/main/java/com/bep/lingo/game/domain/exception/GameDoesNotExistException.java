package com.bep.lingo.game.domain.exception;

//https://github.com/sastani/Hangman/blob/master/src/main/java/com/exceptions/GameDoesNotExistException.java
public final class GameDoesNotExistException extends Exception{
    public GameDoesNotExistException(String id){
        super(String.format("Game with id: %s does not exist.", id));
    }
}

